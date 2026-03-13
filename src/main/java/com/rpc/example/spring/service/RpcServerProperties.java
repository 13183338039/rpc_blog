package com.rpc.example.spring.service;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcServerProperties.java
 * @Description TODO
 * @createTime 2022年04月28日 20:34:00
 */
@Data
@ConfigurationProperties("rpc")
public class RpcServerProperties {

//    private String serviceAddress;

    private int servicePort;

    // 服务注册相关
    private String registryAddress;

    private byte registryType;

}
