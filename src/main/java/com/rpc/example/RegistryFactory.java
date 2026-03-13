package com.rpc.example;

import com.rpc.example.zookeeper.ZookeeperRegistryService;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RegistryFactory.java
 * @Description 简单工厂：根据类型选择使用什么样的注册中心
 * @createTime 2022年04月29日 20:30:00
 */
public class RegistryFactory {

    public static IRegistryService createRegistryService(String address, RegistryType registryType) {
        IRegistryService service = null;

        try {
            switch (registryType) {
                case EUREKA:
                    // todo
                    break;
                case ZOOKEEPER:
                    service = new ZookeeperRegistryService(address);
                    break;
                default:
                    service = new ZookeeperRegistryService(address);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return service;
    }

}
