package com.rpc.example.mapper;

import com.rpc.entity.BlogLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 博客点赞关系 Mapper
 */
@Mapper
public interface BlogLikeMapper {

    /**
     * 新增点赞记录
     */
    int insert(BlogLike like);

    /**
     * 判断是否存在点赞记录
     */
    Integer exists(@Param("blogId") Long blogId, @Param("userId") Long userId);
}

