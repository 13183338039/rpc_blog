package com.rpc.example.loadbalance;

import java.util.List;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName ILoadBalance.java
 * @Description TODO
 * @createTime 2022年04月29日 20:15:00
 */
public interface ILoadBalance<T> {

    T select(List<T> servers);

}
