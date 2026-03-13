package com.markerhub;

import com.markerhub.VueblogApplication;
import com.markerhub.common.exception.CustomException;
import com.rpc.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)//SpringBoot 2.X 默认使用Junit4
@SpringBootTest(classes = VueblogApplication.class)
public class VueblogApplicationTests {

    //如果无法注入RedisTemplate，就使用@Resource试试
    @Autowired
    private StringRedisTemplate stringRedisTemplate;//自带的字符串模板类，用于存储字符串

    @Autowired
    private RedisTemplate redisTemplate;//自带的对象模板类，用于存储对象

    //@Test
    public void test() throws Exception
    {
        // 保存字符串
        stringRedisTemplate.opsForValue().set("username", "redis!!!");
        Logger logger = LoggerFactory.getLogger(VueblogApplicationTests.class);
        String str = stringRedisTemplate.opsForValue().get("username");
        logger.warn(str);
    }

    //@Test
    public void test1() throws Exception {

        User user = new User();
        user.setUsername("hello");
        user.setPassword("12345");

        redisTemplate.opsForValue().set("user_1", user);
        User user1 = (User) redisTemplate.opsForValue().get("user_1");

        System.out.println(user1.getUsername());
    }

    @Test
    public void test2() throws Exception{
        throw new CustomException("这是一个自定义异常");
    }
}