package com.rpc.example;

import com.rpc.example.protocol.NettyServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName NettyRpcProvider.java
 * @Description TODO
 * @createTime 2022年04月23日 20:06:00
 */
//@ComponentScan(basePackages = {
//        "com.rpc.example.mapper",
//        "com.rpc.example.service",
//
//        "com.rpc.example.spring.service",
//
//})

@SpringBootApplication

@ComponentScan(basePackages = {
        "com.rpc.example.service",
        "com.rpc.example.spring.service",
        "com.rpc.example.config"
})
@MapperScan("com.rpc.example.mapper") // ✅ 必须加上
//@MapperScan("com.rpc.example.mapper")
//@Import({
//        com.rpc.example.spring.service.RpcProviderAutoConfiguration.class
//})
public class NettyRpcProvider {

    public static void main(String[] args) {
        SpringApplication.run(NettyRpcProvider.class, args);
        // 启动netty服务端
//        new NettyServer("127.0.0.1", 8081).startNettyServer();
    }
}
