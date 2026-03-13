package com.rpc.example.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName SpringBeanManager.java
 * @Description TODO
 * @createTime 2022年04月23日 21:25:00
 */
@Deprecated
@Component
public class SpringBeanManager implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanManager.applicationContext = applicationContext;
    }

    /*
    根据类获取实例
     */
    public static <T> T getBean(Class<T> clzz) {
        return applicationContext.getBean(clzz);
    }
}
