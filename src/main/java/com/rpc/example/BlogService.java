package com.rpc.example;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpc.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-05-25
 */
//@RemoteService
public interface BlogService {
    IPage<Blog> getPage(Page<Blog> page);
    Blog getById(Long l);
    boolean saveOrUpdate(Blog blog);
    /** 按主键删除博客 */
    boolean removeById(Long id);
    /** 按状态查询已发布博客（如 status=0 表示已发布），用于 ES 全量同步 */
    List<Blog> listByStatus(Integer status);

    /**
     * 用户尝试给文章点赞（幂等）：
     * - 成功或已点过赞都会返回最新的博客数据
     */
    Blog likeBlog(Long blogId, Long userId);

    /**
     * 点赞排行榜，按点赞数倒序返回前若干条
     */
    List<Blog> listTopLiked(Integer limit);
}
