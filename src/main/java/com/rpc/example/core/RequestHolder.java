package com.rpc.example.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RequestHolder.java
 * @Description 当前请求信息
 * @createTime 2022年04月23日 21:40:00
 */
public class RequestHolder {

    /*
    原子性 请求ID
     */
    public static final AtomicLong REQUEST_ID = new AtomicLong();

    /*
    保存请求ID和返回数据的关系
     */
    public static final Map<Long, RpcFuture> REQUEST_MAP = new ConcurrentHashMap<>();
}
