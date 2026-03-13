package com.rpc.example;

import com.rpc.entity.Blog;
import com.rpc.entity.BlogDocument;

import java.util.List;
import java.util.Map;

/**
 * 搜索服务 RPC 接口：全文搜索、同步/删除 ES、统计等
 */
public interface ISearchService {

    /**
     * 检查 Elasticsearch 是否可用
     */
    boolean isElasticsearchAvailable();

    /**
     * 全文搜索（支持高亮、分页）
     */
    Map<String, Object> search(String keyword, int page, int size);

    /**
     * 搜索建议/自动补全
     */
    List<String> suggest(String keyword);

    /**
     * 搜索统计（总文档数、热门关键词等）
     */
    Map<String, Object> getSearchStats();

    /**
     * 同步单篇博客到 Elasticsearch
     */
    void syncBlogToEs(Blog blog);

    /**
     * 从 Elasticsearch 删除博客
     */
    void deleteBlogFromEs(Long blogId);
}
