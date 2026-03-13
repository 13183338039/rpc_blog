package com.rpc.example.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcRequest.java
 * @Description TODO
 * @createTime 2022年04月23日 20:12:00
 */
@Data
public class RpcRequest implements Serializable {

    private String className; // 类名

    private String methodName; //请求目标方法名

    private Object[] params; // 请求参数

    private Class<?>[] paramsTypes; // 参数类型

}
