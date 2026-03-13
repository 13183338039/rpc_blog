package com.rpc.example.spring.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rpc.example.core.RpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName Mediator.java
 * @Description 委托对象
 * @createTime 2022年04月28日 20:23:00
 */
public class Mediator {

    // 定义好映射关系
    // 管理所有bean 和 method的关系
    public static Map<String, BeanMethod> beanMethodMap = new ConcurrentHashMap<>();

    // 构造单例方法
    private volatile static Mediator instance = null;

    private Mediator() {
    }

    public static Mediator getInstance() {
        if (instance == null) {
            synchronized (Mediator.class) {
                if (instance == null) {
                    instance = new Mediator();
                }
            }
        }

        return instance;
    }

    // 根据请求从map里面获取关系进行调用
    public Object processor(RpcRequest request) throws InvocationTargetException, IllegalAccessException {
        String key = request.getClassName() + "." + request.getMethodName();
        BeanMethod beanMethod = beanMethodMap.get(key);
        if (beanMethod == null) {
            return null;
        }
        Object object = beanMethod.getBean();
        Method method = beanMethod.getMethod();
        // 避免对 Spring CGLIB 代理或含 JDK 类型的 params 调用 toString()，在 Java 9+ 会触发 IllegalAccessException
        System.out.println("object.getClass():   " + object.getClass().getName());
        System.out.println("request: " + request.getClassName() + "." + request.getMethodName() + ", paramsCount=" + (request.getParams() == null ? 0 : request.getParams().length));

        Object[] rawParams = request.getParams(); // 一般是 JSONObject 或 Map
        Class<?>[] paramTypes = method.getParameterTypes(); // 获取目标方法参数类型
        Object[] realArgs = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Object raw = rawParams[i];
            System.out.println("types:  "+raw.getClass().getName());
            // 1. 如果 raw 是 JSONObject
            if (raw instanceof JSONObject) {
                realArgs[i] = ((JSONObject) raw).toJavaObject(paramTypes[i]); // fastjson 转换
            }
            // 2. 如果 raw 是 Map
            else if (raw instanceof Map) {
                realArgs[i] = new JSONObject((Map) raw).toJavaObject(paramTypes[i]);
            }
            // 3. 如果是普通类型，比如 String、Integer 等
            else {
                String json = JSON.toJSONString(raw);
//                return JSON.parseObject(json, paramTypes[i]);
                realArgs[i] = JSON.parseObject(json, paramTypes[i]);
            }
            System.out.println("realArgs[i]:   "+realArgs[i].getClass().getName());
        }

//        return method.invoke(object, request.getParams());
        return method.invoke(object, realArgs);
    }
}
