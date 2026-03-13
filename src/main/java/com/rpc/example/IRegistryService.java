package com.rpc.example;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName IRegistryService.java
 * @Description TODO
 * @createTime 2022年04月29日 19:43:00
 */
public interface IRegistryService {


    /**
     * 服务注册
     *
     * @param serviceInfo
     */
    void register(ServiceInfo serviceInfo) throws Exception;

    /**
     * 服务发现
     *
     * @param serviceName
     * @return
     */
    ServiceInfo discovery(String serviceName) throws Exception;

}
