package com.rpc.example.mapper;

import com.rpc.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-05-25
 */
@Mapper
public interface BlogMapper {

    Blog getById(@Param("id") Long id);

    List<Blog> selectPage(@Param("offset") long offset, @Param("size") long size);

    long selectCount();

    int insert(Blog blog);

    int updateById(Blog blog);

    /** 按状态查询博客列表（如 status=0 已发布） */
    List<Blog> selectByStatus(@Param("status") Integer status);

    int deleteById(@Param("id") Long id);

    /**
     * 点赞数自增 1
     */
    int increaseLikeCount(@Param("id") Long id);

    /**
     * 查询点赞排行榜
     */
    List<Blog> selectTopLiked(@Param("limit") int limit);
}
