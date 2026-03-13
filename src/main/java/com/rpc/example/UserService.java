package com.rpc.example;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rpc.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 关注公众号：MarkerHub
 * @since 2020-05-25
 */
public interface UserService  {
    User getAByUsername(String username);

    User getAById(Long id);

    User getAByAccount(String account);

    /** 按邮箱查询用户（注册时校验邮箱是否已存在） */
    User getAByEmail(String email);

    User getById(Long l);

    /** 保存用户（注册时新增），返回带 id 的用户 */
    User saveUser(User user);

    String test1(String s);

    int updateLoginByid(Long id, LocalDateTime lastLogin);

//    User getAByUsername(String username);

//    User getOne(QueryWrapper<User> username);

//    User getById(long l);
}
