package com.markerhub;

import com.markerhub.config.JedisConfig;
import com.markerhub.util.JedisUtil;
import com.markerhub.util.JwtUtils;
import com.rpc.entity.User;
import org.apache.catalina.core.ApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JedisTest {

    @Test
    public void a1() throws InterruptedException {

//        JedisUtil.setObject("JedisUtil1","zsh");
//        String u2=(String)JedisUtil.getObject("JedisUtil1");
//        System.out.println("结束！");

        Jedis jedis=JedisUtil.getJedis();
        jedis.set("JedisUtil1","zsh");
        jedis.set("JedisUtil2","zsss");
        Set<String> keys = jedis.keys("*");
        System.out.println(keys);
        System.out.println("删除键password:"+jedis.del("JedisUtil1"));
        System.out.println("判断键password是否存在："+jedis.exists("JedisUtil1"));
        System.out.println("设置键username的过期时间为5s:"+jedis.expire("JedisUtil2", 8));
        TimeUnit.SECONDS.sleep(1);
        System.out.println("查看键username的剩余生存时间："+jedis.ttl("JedisUtil2"));
        System.out.println("移除键username的生存时间："+jedis.persist("JedisUtil2"));
        System.out.println("查看键username的剩余生存时间："+jedis.ttl("JedisUtil2"));
        System.out.println("查看键username所存储的值的类型："+jedis.type("JedisUtil2"));
        //JedisUtil.close(jedis);
        jedis.close();
    }
}
