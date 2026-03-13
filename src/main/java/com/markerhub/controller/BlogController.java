package com.markerhub.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.markerhub.annotation.OperateLog;
import com.markerhub.annotation.OperateLog;
import com.markerhub.common.lang.Result;
import com.rpc.entity.Blog;
import com.rpc.entity.User;
import com.rpc.entity.UserRegisterRequest;
import com.rpc.example.BlogService;
import com.rpc.example.ISearchService;
import com.rpc.example.UserService;
import com.markerhub.util.ShiroUtil;
import com.rpc.example.annotation.RemoteReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-05-25
 */
@RestController
@Slf4j
public class BlogController {

    @RemoteReference
    BlogService blogService;

    @RemoteReference
    ISearchService searchService;

    @GetMapping("/un/requestbody/{klassId}")
    public String getKlassRelatedTeachers(
            @PathVariable("klassId") Long klassId,
            @RequestParam(value = "type", required = false) String type,
            @RequestBody @Valid UserRegisterRequest userRegisterRequest ) {
        log.info("type:"+type);
        log.info("userRegisterRequest"+userRegisterRequest);
        return "zsh"+klassId;
    }

    @GetMapping("/un/get")
    @OperateLog(operateModule = "用户管理", operateType = "GET", operateDesc = "获取用户")
    public String get(String name){
        //...
        System.out.println(name);
//        log.println("日志");
        return name+"最好了";
    }
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
        System.out.println("blogs");
        Subject subject = SecurityUtils.getSubject();
        // 登录了返回true
        if (subject.isAuthenticated()) {
            System.out.println("blog-success");
//        }else{
//            System.out.println("blog-error");
//        }
        Page page = new Page(currentPage, 5);
        Object o1=blogService.getPage(page);
        System.out.println(o1.getClass().getName());
        IPage pageData = (IPage) o1;

//        JSONObject json = (JSONObject) blogService.getPage(page);
//        Page<Blog> pageData = json.toJavaObject(new TypeReference<Page<Blog>>() {});

        return Result.succ(pageData);
        }else{
            return Result.succ(null);
        }
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        System.out.println("blogs/2");
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");

        return Result.succ(blog);
    }

    /**
     * 博客点赞（需要登录）
     */
    @RequiresAuthentication
    @PostMapping("/blog/{id}/like")
    public Result like(@PathVariable("id") Long id) {
        Long userId = ShiroUtil.getProfile().getId();
        Blog blog = blogService.likeBlog(id, userId);
        Assert.notNull(blog, "该博客不存在或点赞失败");
        Map<String, Object> data = new HashMap<>();
        data.put("id", blog.getId());
        data.put("likeCount", blog.getLikeCount());
        return Result.succ(data);
    }

    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {
        System.out.println("blogs/edit");
//        Assert.isTrue(false, "公开版不能任意编辑！");

        Blog temp = null;
        if(blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
//            a1=blogService.
//            User u1= SecurityUtils.getSubject().getPrincipal();
            System.out.println(ShiroUtil.getProfile().getId());
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        } else {

            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        }

        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", "status");
        blogService.saveOrUpdate(temp);
        // 同步到 Elasticsearch（RPC 调用 provider）
        if (searchService != null && searchService.isElasticsearchAvailable()) {
            searchService.syncBlogToEs(temp);
        }
        return Result.succ(null);
    }

    @RequiresAuthentication
    @DeleteMapping("/blog/{id}")
    public Result delete(@PathVariable(name = "id") Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客不存在");
        Assert.isTrue(blog.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限删除");
        blogService.removeById(id);
        if (searchService != null && searchService.isElasticsearchAvailable()) {
            searchService.deleteBlogFromEs(id);
        }
        return Result.succ(null);
    }

    /**
     * 点赞排行榜，返回点赞数最高的若干篇博客
     */
    @GetMapping("/blogs/top-liked")
    public Result topLiked(@RequestParam(defaultValue = "10") Integer limit) {
        List<Blog> blogs = blogService.listTopLiked(limit);
        return Result.succ(blogs);
    }

    /** 全文搜索 */
    @GetMapping("/search")
    public Result search(@RequestParam String keyword,
                         @RequestParam(defaultValue = "1") Integer currentPage,
                         @RequestParam(defaultValue = "5") Integer pageSize) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Result.fail("搜索关键词不能为空");
        }
        if (searchService == null || !searchService.isElasticsearchAvailable()) {
            Map<String, Object> empty = new HashMap<>();
            empty.put("blogs", new ArrayList<>());
            empty.put("total", 0L);
            empty.put("current", currentPage);
            empty.put("size", pageSize);
            empty.put("totalPages", 0);
            return Result.succ(empty);
        }
        return Result.succ(searchService.search(keyword.trim(), currentPage, pageSize));
    }

    /** 搜索建议/自动补全 */
    @GetMapping("/search/suggest")
    public Result suggest(@RequestParam String keyword) {
        if (searchService == null) {
            return Result.succ(new ArrayList<>());
        }
        return Result.succ(searchService.suggest(keyword != null ? keyword : ""));
    }

    /** 搜索统计 */
    @GetMapping("/search/stats")
    public Result searchStats() {
        if (searchService == null) {
            Map<String, Object> empty = new HashMap<>();
            empty.put("totalBlogs", 0);
            empty.put("hotKeywords", new ArrayList<>());
            return Result.succ(empty);
        }
        return Result.succ(searchService.getSearchStats());
    }

    /** 手动全量同步博客到 ES（需登录） */
    @RequiresAuthentication
    @PostMapping("/search/sync-all")
    public Result syncAllBlogs() {
        if (searchService == null || !searchService.isElasticsearchAvailable()) {
            return Result.fail("Elasticsearch 不可用");
        }
        try {
            List<Blog> blogs = blogService.listByStatus(0);
            if (blogs.isEmpty()) {
                return Result.fail("没有需要同步的博客（status=0）");
            }
            int success = 0, fail = 0;
            List<String> errors = new ArrayList<>();
            for (Blog blog : blogs) {
                try {
                    searchService.syncBlogToEs(blog);
                    success++;
                } catch (Exception e) {
                    fail++;
                    errors.add("ID " + blog.getId() + ": " + e.getMessage());
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("total", blogs.size());
            result.put("success", success);
            result.put("fail", fail);
            if (!errors.isEmpty()) {
                result.put("errors", errors.subList(0, Math.min(10, errors.size())));
            }
            return Result.succ(result);
        } catch (Exception e) {
            return Result.fail("同步失败: " + e.getMessage());
        }
    }
}
