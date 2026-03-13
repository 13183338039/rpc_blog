package com.rpc.example.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RemoteService.java
 * @Description TODO
 * @createTime 2022年04月28日 20:09:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RemoteService {

}
