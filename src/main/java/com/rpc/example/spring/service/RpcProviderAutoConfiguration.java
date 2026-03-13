package com.rpc.example.spring.service;

import com.rpc.example.IRegistryService;
import com.rpc.example.RegistryFactory;
import com.rpc.example.RegistryType;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.net.UnknownHostException;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcProviderAutoConfiguration.java
 * @Description 自动配置类
 * @createTime 2022年04月28日 20:38:00
 */
@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
//@AutoConfigureAfter(name = {
//        "org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration",
//        "org.apache.shiro.spring.config.ShiroAnnotationProcessorConfiguration"
//})
public class RpcProviderAutoConfiguration {

    @Bean
    public SpringRpcProviderBean springRpcProviderBean(RpcServerProperties rpcServerProperties) throws UnknownHostException {
        // 服务注册的时候需要传递注册中心
        System.out.println("获取注册中心 并传递");
        IRegistryService registryService = RegistryFactory.createRegistryService(rpcServerProperties.getRegistryAddress(), RegistryType.findByCode(rpcServerProperties.getRegistryType()));

        return new SpringRpcProviderBean(rpcServerProperties.getServicePort(), registryService);
    }
}
