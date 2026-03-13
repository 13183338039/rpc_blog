package com.markerhub;

import com.markerhub.VueblogApplication;
import com.rpc.entity.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

@RunWith(SpringRunner.class)//SpringBoot 2.X 默认使用Junit4
@SpringBootTest(classes = VueblogApplication.class)
public class redistemplate {

    //如果无法注入RedisTemplate，就使用@Resource试试
    @Autowired
    private StringRedisTemplate stringRedisTemplate;//自带的字符串模板类，用于存储字符串

    @Autowired
    private RedisTemplate redisTemplate;//自带的对象模板类，用于存储对象

    @Test
    public void test() throws Exception
    {
        // 保存字符串
        stringRedisTemplate.opsForValue().set("username", "redis!!!");
        Logger logger = LoggerFactory.getLogger(redistemplate.class);
        String str = stringRedisTemplate.opsForValue().get("username");
        logger.warn(str);
    }

    @Test
    public void test1() throws Exception {
//1.类对象
        User user = new User();
        user.setUsername("hello");
        user.setPassword("12345");

        redisTemplate.opsForValue().set("user_1", user);
        User user1 = (User) redisTemplate.opsForValue().get("user_1");
        System.out.println(user1.getUsername());
//2.string
        Map valueMap = new HashMap();
        valueMap.put("valueMap1","map1");
        valueMap.put("valueMap2","map200");
        valueMap.put("valueMap3","map3");
        redisTemplate.opsForValue().multiSet(valueMap);
        String s1=(String)redisTemplate.opsForValue().get("valueMap2");
        System.out.println("valueMap2的长度是"+s1.length());
//3.hashmap
        // 相当于 HSET user:1001 name "Alice"
        redisTemplate.opsForHash().put("user:1001", "name", "Alice");
        // 可以再加个字段
        redisTemplate.opsForHash().put("user:1001", "age", 30);
        Object name = redisTemplate.opsForHash().get("user:1001", "name");
        System.out.println("获取user:1001 hash表中属性name的值:"+name);  // Alice
//4.list类型的数据
        // 从左边插入（类似栈）
        redisTemplate.opsForList().leftPush("myList", "value1");
        // 从右边插入（类似队列）
        redisTemplate.opsForList().rightPush("myList", "value2");
        // 插入多个元素
        redisTemplate.opsForList().rightPushAll("myList", "a", "b", "c");
        // 获取指定范围的元素（类似 subList）
        // 0 到 -1 表示所有元素
        List<Object> list = redisTemplate.opsForList().range("myList", 0, -1);
        int n=list.size();
        for(int i=0;i<n;i++){
            System.out.println("获取myList列表中的值:"+list.get(i));  // Alice
        }
//5.set
        redisTemplate.opsForSet().add("mySet", "A");
        redisTemplate.opsForSet().add("mySet", "B", "C", "D");
        Set<Object> members = redisTemplate.opsForSet().members("mySet");
        System.out.println("mySet的大小是"+members.size());
//6.zset
        redisTemplate.opsForZSet().add("myZSet", "user1", 100.0);
        redisTemplate.opsForZSet().add("myZSet", "user2", 95.5);
        Double score = redisTemplate.opsForZSet().score("myZSet", "user1");
        System.out.println("myZSet:user1的分数是"+score);
        // 正序排名（分数小的排前面）
        Long rank = redisTemplate.opsForZSet().rank("myZSet", "user1");
        Set<Object> top3Asc = redisTemplate.opsForZSet().range("myZSet", 0, 2);



    }
    @Test
    public void testRedisListPush() {
        String redisKey = "testGoodsKey";
        List<String> redisValues = Arrays.asList("10002001", "10002002");
        // 使用管道向redis list结构中批量插入元素
        redisTemplate.executePipelined((RedisConnection redisConnection) -> {
            // 打开管道
            redisConnection.openPipeline();
            // 给本次管道内添加，一次性执行的多条命令
            for (String redisValue : redisValues) {
                redisConnection.rPush(redisKey.getBytes(), redisValue.getBytes());
            }
            return null;
        });

    }
    @Test
    public void testRedisListPop() {
        String redisKey = "testGoodsKey";
        // 使用管道从redis list结构中批量获取元素
        List<Object> objects = redisTemplate.executePipelined((RedisConnection redisConnection) -> {
            // 打开管道
            redisConnection.openPipeline();
            for (int i = 0; i < 2; i++) {
                redisConnection.rPop(redisKey.getBytes());
            }
            return null;
        }, redisTemplate.getStringSerializer());
        System.out.println(objects);
    }




}