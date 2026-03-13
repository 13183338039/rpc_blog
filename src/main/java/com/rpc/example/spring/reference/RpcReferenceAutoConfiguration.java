package com.rpc.example.spring.reference;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcReferenceAutoConfiguration.java
 * @Description TODO
 * @createTime 2022年04月28日 21:40:00
 */
@Configuration
public class RpcReferenceAutoConfiguration implements EnvironmentAware {

    private Environment environment;

    @PostConstruct
    public void init() {
        System.out.println("RpcReferenceAutoConfiguration" + " _init");
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SpringRpcReferencePostProcessor postProcessor() {
        System.out.println("SpringRpcReferencePostProcessor postProcessor");
        RpcClientProperties rpcClientProperties = new RpcClientProperties();
        // 使用注册中心就不需要手动获取对应的服务端口号及地址了
//        rpcClientProperties.setServiceAddress(this.environment.getProperty("rpc.client.serviceAddress"));
//        rpcClientProperties.setServicePort(Integer.parseInt(this.environment.getProperty("rpc.client.servicePort")));

        rpcClientProperties.setRegistryAddress(this.environment.getProperty("rpc.client.registryAddress"));
        rpcClientProperties.setRegistryType(Byte.parseByte(this.environment.getProperty("rpc.client.registryType")));

        return new SpringRpcReferencePostProcessor(rpcClientProperties);
    }
}
