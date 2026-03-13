package com.rpc.example.service;

//import com.rpc.example.IUserService;
//import com.rpc.example.annotation.RemoteService;
//import org.springframework.stereotype.Service;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName UserServiceImpl.java
 * @Description TODO
 * @createTime 2022年04月23日 19:58:00
 */
//自定义注解 远程接口
//@RemoteService
//@Service
//public class UserServiceImpl implements IUserService {
//    @Override
//    public String saveUser(String name) {
//        System.out.println("begin save user:" + name);
//        return "save user success：" + name;
//    }
//}
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rpc.entity.User;
import com.rpc.example.annotation.RemoteService;
import com.rpc.example.mapper.UserMapper;
import com.rpc.example.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
public class UserServiceImpl  implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getAByUsername(String username ) {
        return userMapper.getByUsername(username); // 调用 UserMapper 中的方法获取属性 a
    }

    @Override
    public User getAById(Long id) {
        return userMapper.getById(id); // 调用 UserMapper 中的方法获取属性 a
    }

    @Override
    public User getById(Long l) {
        return userMapper.getById(l); // 调用 UserMapper 中的方法获取属性 a
    }

    @Override
    public String test1(String s){
        return "111+"+s+userMapper.getByUsername("zhangshuhao").getEmail();
    }
    @Override
    public User getAByAccount(String account){
        return userMapper.getByUsername(account);
    }

    @Override
    public User getAByEmail(String email) {
        return userMapper.getByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    public int updateLoginByid(Long id, LocalDateTime lastLogin) {
        return userMapper.updateLoginByid(id, lastLogin);
    }

}