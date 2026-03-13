package com.rpc.example.spring.reference;

import com.fasterxml.jackson.databind.util.ClassUtil;
import com.rpc.example.annotation.RemoteReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName SpringRpcReferencePosetProcessor.java
 * @Description TODO
 * @createTime 2022年04月28日 21:16:00
 */
public class SpringRpcReferencePostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {

    private ApplicationContext applicationContext;

    private ClassLoader classLoader;

    private RpcClientProperties rpcClientProperties;

    public SpringRpcReferencePostProcessor(RpcClientProperties rpcClientProperties) {
        this.rpcClientProperties = rpcClientProperties;
    }

    @PostConstruct
    public void init() {
        System.out.println("SpringRpcReferencePostProcessor" + " _init");
    }

    // 保存发布的引用Bean的信息
    private final Map<String, BeanDefinition> rpcRefBeanDefinition = new ConcurrentHashMap<>();

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // spring 容器加载了bean的定义文件之后，在bean实例化之前执行
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("通过反射获取实例对象,分析其中是否带有rpc注解");
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String className = beanDefinition.getBeanClassName();
            if (className != null) {
                //System.out.println("className:      " + className);
                Class<?> clazz = ClassUtils.resolveClassName(className, this.classLoader);
                // 遍历字段解析
                ReflectionUtils.doWithFields(clazz, this::parseRpcReference);
            }
        }

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        this.rpcRefBeanDefinition.forEach((beanName, beanDefinition) -> {
            if (applicationContext.containsBean(beanName)) {
                System.out.println("SpringContext already registry: " + beanName);
            }
            registry.registerBeanDefinition(beanName, beanDefinition);
            System.out.println(" registry bean: " + beanName);
        });
    }

    // 遍历字段解析
    private void parseRpcReference(Field field) {
        //System.out.println("SpringRpcReferenceBean parseRpcReference");
        //System.out.println("field-1:      "+field.getName());
        RemoteReference annotation = AnnotationUtils.getAnnotation(field, RemoteReference.class);
        if (annotation != null) {
            // 不为空则构建实例注入
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.
                    genericBeanDefinition(SpringRpcReferenceBean.class);
            builder.setInitMethodName("init");
            builder.addPropertyValue("interfaceClass", field.getType());
            // 使用注册中心的地址，不需要手动注入地址及端口号
//            builder.addPropertyValue("serviceAddress", rpcClientProperties.getServiceAddress());
//            builder.addPropertyValue("servicePort", rpcClientProperties.getServicePort());

            builder.addPropertyValue("registryAddress", rpcClientProperties.getRegistryAddress());
            builder.addPropertyValue("registryType", rpcClientProperties.getRegistryType());

            BeanDefinition beanDefinition = builder.getBeanDefinition();
            System.out.println("带有rpc注解:      "+field.getName());
            rpcRefBeanDefinition.put(field.getName(), beanDefinition);
        }
    }

}
