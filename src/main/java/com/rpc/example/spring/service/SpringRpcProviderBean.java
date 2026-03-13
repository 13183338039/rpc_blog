package com.rpc.example.spring.service;

import com.rpc.example.IRegistryService;
import com.rpc.example.ServiceInfo;
import com.rpc.example.annotation.RemoteService;
import com.rpc.example.protocol.NettyServer;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName SpringRpcProviderBean.java
 * @Description TODO
 * @createTime 2022年04月28日 20:12:00
 */

public class SpringRpcProviderBean implements InitializingBean, BeanPostProcessor {

    private final int serverPort;

    private final String serverAddress;

    // 服务注册相关
    private final IRegistryService registryService; // 服务注册中心


    public SpringRpcProviderBean(int serverPort, IRegistryService registryService) throws UnknownHostException {
        this.serverPort = serverPort;
        // 获取服务器的IP地址
        InetAddress address = InetAddress.getLocalHost();
        System.out.println("网络地址：" + address.getHostAddress());
        this.serverAddress = address.getHostAddress();
        this.registryService = registryService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("尝试开启netty server");
        new Thread(() -> {
            new NettyServer(this.serverAddress, this.serverPort).startNettyServer();
        }).start();
    }

    // 任何Bean装载到spring 容器的时候都会回调
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //System.out.println("postProcessAfterInitialization");
        // 只要目标类声明了RemoteService注解，则需要把该服务发布到网络上 允许被调用
        System.out.println("判断组件:   " + beanName);

        // 获取真实目标类（去掉 Spring AOP 代理），避免 @Transactional 等导致注解丢失
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (targetClass == null) {
            return bean;
        }

        if (targetClass.isAnnotationPresent(RemoteService.class)) {
            System.out.println("该组件带rpc注解:   " + beanName);
            // 得到所有方法（基于目标类）
            Method[] methods = targetClass.getMethods();

            //我们这里需要把具体的发布映射关系保存下来
            for (Method method : methods) {
                String serviceName = targetClass.getInterfaces()[0].getName();
                String key = serviceName + "." + method.getName();
                BeanMethod beanMethod = new BeanMethod();
                // 这里仍然使用代理对象 bean，以保证 @Transactional 等 AOP 增强生效
                beanMethod.setBean(bean);
                beanMethod.setMethod(method);
                System.out.println("插入键值对:  (" + key + ",方法)");
                Mediator.beanMethodMap.put(key, beanMethod);

                //服务注册相关
                //发布到远程服务端
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setServiceAddress(this.serverAddress);
                serviceInfo.setServicePort(this.serverPort);
                serviceInfo.setServiceName(serviceName);
                try {
                    registryService.register(serviceInfo); // 注册服务
                } catch (Exception e) {
                    System.out.println(serviceName + "registry failed");
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
