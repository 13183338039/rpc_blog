package com.rpc.example.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.rpc.entity.Blog;
import com.rpc.entity.BlogDocument;
import com.rpc.example.ISearchService;
import com.rpc.example.annotation.RemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 搜索服务实现 - 通过 RPC 暴露给 vueblog-java，使用 Elasticsearch Java Client
 */
@RemoteService
@Service
public class SearchServiceImpl implements ISearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Autowired(required = false)
    private ElasticsearchClient elasticsearchClient;

    private static final String INDEX_NAME = "blog_index";

    @Override
    public boolean isElasticsearchAvailable() {
        return elasticsearchClient != null;
    }

    @PostConstruct
    public void initIndex() {
        if (elasticsearchClient == null) return;
        try {
            boolean exists = elasticsearchClient.indices().exists(e -> e.index(INDEX_NAME)).value();
            if (!exists) {
                elasticsearchClient.indices().create(c -> c
                        .index(INDEX_NAME)
                        .mappings(m -> m
                                .properties("id", Property.of(p -> p.long_(l -> l)))
                                .properties("userId", Property.of(p -> p.long_(l -> l)))
                                .properties("title", Property.of(p -> p.text(t -> t)))
                                .properties("description", Property.of(p -> p.text(t -> t)))
                                .properties("content", Property.of(p -> p.text(t -> t)))
                                .properties("tags", Property.of(p -> p.keyword(k -> k)))
                                .properties("created", Property.of(p -> p.date(d -> d.format("yyyy-MM-dd HH:mm:ss"))))
                                .properties("status", Property.of(p -> p.integer(i -> i)))
                        )
                );
                log.info("Elasticsearch 索引 [" + INDEX_NAME + "] 创建成功");
            }
        } catch (Exception e) {
            log.warn("创建 Elasticsearch 索引失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> search(String keyword, int page, int size) {
        Map<String, Object> empty = new HashMap<>();
        empty.put("blogs", new ArrayList<>());
        empty.put("total", 0L);
        empty.put("current", page);
        empty.put("size", size);
        empty.put("totalPages", 0);
        if (elasticsearchClient == null) return empty;

        try {
            Query multiMatchQuery = Query.of(q -> q
                    .multiMatch(MultiMatchQuery.of(m -> m
                            .query(keyword)
                            .fields("title^2", "content", "description", "tags")
                            .fuzziness("AUTO")
                            .type(co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.BestFields)
                    ))
            );
            Map<String, HighlightField> highlightFields = new HashMap<>();
            highlightFields.put("title", HighlightField.of(f -> f.preTags("<em style='color:red'>").postTags("</em>")));
            highlightFields.put("description", HighlightField.of(f -> f.preTags("<em style='color:red'>").postTags("</em>")));
            highlightFields.put("content", HighlightField.of(f -> f.preTags("<em style='color:red'>").postTags("</em>")));
            Highlight highlight = Highlight.of(h -> h.fields(highlightFields));

            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(INDEX_NAME)
                    .query(multiMatchQuery)
                    .highlight(highlight)
                    .from((page - 1) * size)
                    .size(size)
            );

            SearchResponse<BlogDocument> searchResponse = elasticsearchClient.search(searchRequest, BlogDocument.class);
            List<BlogDocument> blogs = new ArrayList<>();
            for (Hit<BlogDocument> hit : searchResponse.hits().hits()) {
                BlogDocument doc = hit.source();
                if (doc == null) continue;
                if (hit.highlight() != null) {
                    Map<String, List<String>> highlights = hit.highlight();
                    if (highlights.containsKey("title") && !highlights.get("title").isEmpty())
                        doc.setTitle(highlights.get("title").get(0));
                    if (highlights.containsKey("description") && !highlights.get("description").isEmpty())
                        doc.setDescription(highlights.get("description").get(0));
                    if (highlights.containsKey("content") && !highlights.get("content").isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (String fragment : highlights.get("content")) sb.append(fragment);
                        String content = sb.toString();
                        doc.setContent(content.length() > 500 ? content.substring(0, 500) + "..." : content);
                    }
                } else {
                    String content = doc.getContent();
                    if (content != null && content.length() > 500)
                        doc.setContent(content.substring(0, 500) + "...");
                }
                blogs.add(doc);
            }

            long total = searchResponse.hits().total() != null ? searchResponse.hits().total().value() : 0;
            Map<String, Object> result = new HashMap<>();
            result.put("blogs", blogs);
            result.put("total", total);
            result.put("current", page);
            result.put("size", size);
            result.put("totalPages", size > 0 ? (int) Math.ceil((double) total / size) : 0);
            return result;
        } catch (ElasticsearchException e) {
            if (e.response().error().type().equals("index_not_found_exception")) {
                try { initIndex(); } catch (Exception ex) { log.warn("initIndex: " + ex.getMessage()); }
            }
            log.error("Elasticsearch 搜索失败: " + e.getMessage());
            return empty;
        } catch (Exception e) {
            log.error("Elasticsearch 搜索失败: " + e.getMessage());
            return empty;
        }
    }

    @Override
    public List<String> suggest(String keyword) {
        List<String> suggestions = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty() || elasticsearchClient == null) return suggestions;
        try {
            Query query = Query.of(q -> q.multiMatch(MultiMatchQuery.of(m -> m.query(keyword).fields("title", "content", "tags"))));
            SearchRequest searchRequest = SearchRequest.of(s -> s.index(INDEX_NAME).query(query).size(10));
            SearchResponse<BlogDocument> searchResponse = elasticsearchClient.search(searchRequest, BlogDocument.class);
            Set<String> titleWords = new HashSet<>();
            for (Hit<BlogDocument> hit : searchResponse.hits().hits()) {
                BlogDocument doc = hit.source();
                if (doc != null && doc.getTitle() != null && doc.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                    for (String word : doc.getTitle().split(" ")) {
                        if (word.toLowerCase().contains(keyword.toLowerCase()) && word.length() > keyword.length())
                            titleWords.add(word);
                    }
                }
            }
            suggestions.addAll(titleWords);
            return suggestions.subList(0, Math.min(5, suggestions.size()));
        } catch (Exception e) {
            return suggestions;
        }
    }

    @Override
    public Map<String, Object> getSearchStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            if (elasticsearchClient != null) {
                SearchRequest searchRequest = SearchRequest.of(s -> s
                        .index(INDEX_NAME)
                        .size(0)
                        .aggregations("hot_tags", a -> a.terms(t -> t.field("tags").size(5)))
                );
                SearchResponse<BlogDocument> searchResponse = elasticsearchClient.search(searchRequest, BlogDocument.class);
                long total = searchResponse.hits().total() != null ? searchResponse.hits().total().value() : 0;
                stats.put("totalBlogs", total);
                List<String> hotKeywords = new ArrayList<>();
                if (searchResponse.aggregations() != null && searchResponse.aggregations().containsKey("hot_tags")) {
                    StringTermsAggregate tagsAgg = searchResponse.aggregations().get("hot_tags").sterms();
                    if (tagsAgg != null && tagsAgg.buckets() != null && tagsAgg.buckets().array() != null) {
                        for (StringTermsBucket bucket : tagsAgg.buckets().array()) {
                            if (bucket.key() != null && !bucket.key().stringValue().isEmpty())
                                hotKeywords.add(bucket.key().stringValue());
                        }
                    }
                }
                if (hotKeywords.isEmpty()) hotKeywords = Arrays.asList("Spring Boot", "Java", "Vue", "Elasticsearch", "博客");
                stats.put("hotKeywords", hotKeywords);
            } else {
                stats.put("totalBlogs", 0);
                stats.put("hotKeywords", Arrays.asList("Java", "Vue", "Elasticsearch", "博客"));
            }
        } catch (Exception e) {
            log.error("获取搜索统计失败: " + e.getMessage());
            stats.put("totalBlogs", 0);
            stats.put("hotKeywords", Arrays.asList("Java", "Vue", "Elasticsearch", "博客"));
        }
        return stats;
    }

    @Override
    public void syncBlogToEs(Blog blog) {
        if (elasticsearchClient == null) return;
        try {
            BlogDocument document = new BlogDocument();
            document.setId(blog.getId());
            document.setUserId(blog.getUserId());
            document.setTitle(blog.getTitle());
            document.setDescription(blog.getDescription());
            document.setContent(blog.getContent());
            document.setCreated(blog.getCreated());
            document.setStatus(blog.getStatus());
            document.setTags(document.getTags() != null ? document.getTags() : new ArrayList<>());

            try {
                if (!elasticsearchClient.indices().exists(e -> e.index(INDEX_NAME)).value())
                    initIndex();
            } catch (Exception ex) { log.warn("检查索引: " + ex.getMessage()); }

            elasticsearchClient.index(i -> i.index(INDEX_NAME).id(document.getId().toString()).document(document));
            log.info("博客已同步到 ES [ID: {}]", document.getId());
        } catch (Exception e) {
            log.error("同步博客到 ES 失败 [ID: {}]: {}", blog.getId(), e.getMessage());
        }
    }

    @Override
    public void deleteBlogFromEs(Long blogId) {
        if (elasticsearchClient == null) return;
        try {
            elasticsearchClient.delete(d -> d.index(INDEX_NAME).id(blogId.toString()));
        } catch (Exception e) {
            log.error("从 ES 删除博客失败: " + e.getMessage());
        }
    }
}
