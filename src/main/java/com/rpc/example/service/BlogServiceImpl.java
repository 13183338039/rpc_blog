package com.rpc.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpc.entity.Blog;
import com.rpc.entity.BlogLike;
import com.rpc.example.BlogService;
import com.rpc.example.annotation.RemoteService;
import com.rpc.example.mapper.BlogLikeMapper;
import com.rpc.example.mapper.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-05-25
 */
@RemoteService
@Service
public class BlogServiceImpl implements BlogService {
    private static final String BLOG_LIKE_RANK_KEY = "blog:like:rank";

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogLikeMapper blogLikeMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public IPage<Blog> getPage(Page<Blog> page) {
        long total = blogMapper.selectCount();
        long offset = (page.getCurrent() - 1) * page.getSize();
        page.setRecords(blogMapper.selectPage(offset, page.getSize()));
        page.setTotal(total);
        return page;
    }

    @Override
    public Blog getById(Long l) {
        return blogMapper.getById(l);
    }

    @Override
    public boolean saveOrUpdate(Blog blog) {
        if (blog.getId() == null) {
            return blogMapper.insert(blog) > 0;
        } else {
            return blogMapper.updateById(blog) > 0;
        }
    }

    @Override
    public boolean removeById(Long id) {
        return blogMapper.deleteById(id) > 0;
    }

    @Override
    public List<Blog> listByStatus(Integer status) {
        return blogMapper.selectByStatus(status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Blog likeBlog(Long blogId, Long userId) {
        if (blogId == null || userId == null) {
            return null;
        }
        try {
            // 1. 插入点赞关系（表 m_blog_like，依赖唯一索引保证同一用户只能点赞一次）
            BlogLike like = new BlogLike();
            like.setBlogId(blogId);
            like.setUserId(userId);
            like.setCreated(LocalDateTime.now());
            blogLikeMapper.insert(like);

            // 2. 数据库点赞数 +1（可选，方便直接从表里看）
            blogMapper.increaseLikeCount(blogId);

            // 3. Redis ZSet 中该文章点赞数 +1
            stringRedisTemplate.opsForZSet()
                    .incrementScore(BLOG_LIKE_RANK_KEY, blogId.toString(), 1.0);
        } catch (DuplicateKeyException e) {
            // 唯一索引冲突，说明这个用户已经给这篇文章点过赞了
            // 为了幂等性，直接视为成功，不再抛异常
        }
        // 无论是首次点赞还是重复点赞，最后统一返回最新的博客数据
        return blogMapper.getById(blogId);
    }


    @Override
    public List<Blog> listTopLiked(Integer limit) {
        int top = (limit == null || limit <= 0) ? 10 : limit;
        // 1. 先尝试从 Redis ZSet 中取点赞排行榜
        Set<ZSetOperations.TypedTuple<String>> tuples =
                stringRedisTemplate.opsForZSet()
                        .reverseRangeWithScores(BLOG_LIKE_RANK_KEY, 0, top - 1);

        List<Blog> result = new ArrayList<>();
        if (tuples != null && !tuples.isEmpty()) {
            for (ZSetOperations.TypedTuple<String> tuple : tuples) {
                String blogIdStr = tuple.getValue();
                if (blogIdStr == null) {
                    continue;
                }
                Long blogId = Long.valueOf(blogIdStr);
                Blog blog = blogMapper.getById(blogId);
                if (blog != null && tuple.getScore() != null) {
                    blog.setLikeCount(Objects.requireNonNull(tuple.getScore()).intValue());
                    result.add(blog);
                }
            }
            return result;
        }

        // 2. 如果 Redis 里暂时没有数据，降级使用数据库的 like_count 排行
        return blogMapper.selectTopLiked(top);
    }
}
