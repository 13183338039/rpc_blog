package com.rpc.example.mapper;

import com.rpc.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-05-25
 */
@Mapper
public interface UserMapper {

    User getByUsername(@Param("username") String username);

    User getById(@Param("id") Long id);

    User getByEmail(@Param("email") String email);

    int insert(User user);

    int updateLoginByid(@Param("id") Long id, @Param("lastLogin") LocalDateTime lastLogin);
}
