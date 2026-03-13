package com.rpc.example;

import lombok.Data;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName ServiceInfo.java
 * @Description TODO
 * @createTime 2022年04月29日 19:44:00
 */
@Data
public class ServiceInfo {

    private String serviceName;

    private String serviceAddress;

    private int servicePort;

}
