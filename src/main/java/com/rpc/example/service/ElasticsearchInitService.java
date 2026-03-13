package com.rpc.example.service;

import com.rpc.entity.Blog;
import com.rpc.example.BlogService;
import com.rpc.example.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Elasticsearch 初始化：应用启动后将已发布博客同步到 ES
 */
@Service
public class ElasticsearchInitService {

    @Autowired
    private BlogService blogService;

    @Autowired
    private ISearchService searchService;

    @EventListener(ApplicationReadyEvent.class)
    public void syncAllBlogsToElasticsearch() {
        if (!searchService.isElasticsearchAvailable()) {
            System.err.println("⚠ Elasticsearch 不可用，跳过博客同步。");
            return;
        }
        System.out.println("========================================");
        System.out.println("ElasticsearchInitService: 开始同步已发布博客...");
        System.out.println("========================================");
        try {
            List<Blog> blogs = blogService.listByStatus(0);
            System.out.println("已发布博客数量: " + blogs.size());
            if (blogs.isEmpty()) {
                System.out.println("无待同步数据（status=0）。");
                return;
            }
            int success = 0, fail = 0;
            for (Blog blog : blogs) {
                try {
                    searchService.syncBlogToEs(blog);
                    success++;
                    if (success % 10 == 0) {
                        System.out.println("已同步 " + success + " 篇...");
                    }
                } catch (Exception e) {
                    fail++;
                    System.err.println("同步失败 [ID:" + blog.getId() + "] " + e.getMessage());
                }
            }
            System.out.println("========================================");
            System.out.println("✓ 同步完成: 成功 " + success + ", 失败 " + fail);
            System.out.println("========================================");
        } catch (Exception e) {
            System.err.println("Elasticsearch 初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
