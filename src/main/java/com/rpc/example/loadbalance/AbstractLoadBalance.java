package com.rpc.example.loadbalance;

import com.rpc.example.ServiceInfo;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName AbstratLoadBalance.java
 * @Description TODO
 * @createTime 2022年04月29日 20:16:00
 */
public abstract class AbstractLoadBalance implements ILoadBalance<ServiceInstance<ServiceInfo>> {

    // 模板方法
    @Override
    public ServiceInstance<ServiceInfo> select(List<ServiceInstance<ServiceInfo>> servers) {
        if (servers == null || servers.size() == 0) {
            return null;
        }
        if (servers.size() == 1) {
            return servers.get(0);
        }
        // 其他的由具体的子类实现
        return doSelect(servers);
    }

    protected abstract ServiceInstance<ServiceInfo> doSelect(List<ServiceInstance<ServiceInfo>> servers);
}
