package com.rpc.example.spring.reference;

import lombok.Data;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcClientProperties.java
 * @Description TODO
 * @createTime 2022年04月28日 21:35:00
 */
@Data
public class RpcClientProperties {

    private String serviceAddress = "192.168.183.1";

    private int servicePort = 20880;

    private byte registryType;

    private String registryAddress;

}
