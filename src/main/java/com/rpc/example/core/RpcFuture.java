package com.rpc.example.core;

import io.netty.util.concurrent.Promise;
import lombok.Data;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcFuture.java
 * @Description 异步返回对象 接受返回数据使用
 * @createTime 2022年04月23日 21:39:00
 */
@Data
public class RpcFuture<T> {

    private Promise<T> promise;

    public RpcFuture(Promise<T> promise) {
        this.promise = promise;
    }

}
