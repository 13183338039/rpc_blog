package com.markerhub;

import com.rpc.example.spring.service.RpcProviderAutoConfiguration;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroAnnotationProcessorAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication

@Import({
        com.rpc.example.spring.reference.RpcReferenceAutoConfiguration.class,
        com.markerhub.controller.RpcController.class
})
//// 启动kafka的功能
//@EnableKafka
//// 为了测试kafka,我们可以周期性的发送消息到消息队列
//// 使用SpringBoot自带的调度工具即可
//@EnableScheduling
public class VueblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(VueblogApplication.class, args);
    }

}
