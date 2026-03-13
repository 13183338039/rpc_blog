package com.rpc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 博客点赞关系
 * 对应表：m_blog_like（包含 blog_id、user_id，唯一索引 uk_blog_user）
 */
@Data
@TableName("m_blog_like")
public class BlogLike implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long blogId;

    private Long userId;

    private LocalDateTime created;
}

