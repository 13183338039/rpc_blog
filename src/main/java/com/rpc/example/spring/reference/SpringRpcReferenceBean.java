package com.rpc.example.spring.reference;

import com.rpc.example.IRegistryService;
import com.rpc.example.RegistryFactory;
import com.rpc.example.RegistryType;
import org.springframework.beans.factory.FactoryBean;

import javax.annotation.PostConstruct;
import java.lang.reflect.Proxy;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName SpringRpcReferenceBean.java
 * @Description 消费端需要使用动态代理 工厂Bean
 * @createTime 2022年04月28日 20:50:00
 */
public class SpringRpcReferenceBean implements FactoryBean<Object> {

    private Object object;

//    private String serviceAddress;
//
//    private int servicePort;

    private Class<?> interfaceClass;

    //服务注册地址
    private String registryAddress;

    private byte registryType;

//    @PostConstruct
//    public void init() {
//        System.out.println("RpcReferenceAutoConfiguration" + " _init");
//    }

    @Override
    public Object getObject() throws Exception {
        return object;
    }

    public void init() {
        // 传入注册中心
        System.out.println("消费端:    传入注册中心");
        IRegistryService registryService = RegistryFactory.createRegistryService(this.registryAddress, RegistryType.findByCode(this.registryType));

        this.object = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new RpcInvokerProxy(registryService));
//                new RpcInvokerProxy(serviceAddress, servicePort));
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClass;
    }

//    public String getServiceAddress() {
//        return serviceAddress;
//    }
//
//    public void setServiceAddress(String serviceAddress) {
//        this.serviceAddress = serviceAddress;
//    }
//
//    public int getServicePort() {
//        return servicePort;
//    }
//
//    public void setServicePort(int servicePort) {
//        this.servicePort = servicePort;
//    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void setRegistryType(byte registryType) {
        this.registryType = registryType;
    }
}
