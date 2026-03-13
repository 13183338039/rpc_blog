package com.rpc.example.spring.service;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName BeanMethod.java
 * @Description 映射关系
 * @createTime 2022年04月28日 20:21:00
 */
@Data
public class BeanMethod {

    private Object bean;

    private Method method;

}
