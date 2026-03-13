package com.rpc.example.loadbalance;

import com.rpc.example.ServiceInfo;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Random;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RandomLoadBalance.java
 * @Description 随机负载均衡
 * @createTime 2022年04月29日 20:19:00
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected ServiceInstance<ServiceInfo> doSelect(List<ServiceInstance<ServiceInfo>> servers) {
        int len = servers.size();
        Random random = new Random();
        return servers.get(random.nextInt(len));
    }
}
