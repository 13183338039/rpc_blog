package com.rpc.example.zookeeper;

import com.rpc.example.IRegistryService;
import com.rpc.example.ServiceInfo;
import com.rpc.example.loadbalance.ILoadBalance;
import com.rpc.example.loadbalance.RandomLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.List;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName ZookeeperRegistryService.java
 * @Description TODO
 * @createTime 2022年04月29日 19:49:00
 */
public class ZookeeperRegistryService implements IRegistryService {

    private static final String REGISTRY_PATH = "/registry";

    // curator 中提供的服务注册与发现的封装
    private final ServiceDiscovery<ServiceInfo> serviceDiscovery;

    // 负载均衡
    private ILoadBalance<ServiceInstance<ServiceInfo>> loadBalance;

    public ZookeeperRegistryService(String registryAddress) throws Exception {
        // 创建curator客户端
        System.out.println("Zookeeper 注册服务创建!"+registryAddress);
        CuratorFramework client = CuratorFrameworkFactory
                .newClient(registryAddress, new ExponentialBackoffRetry(1000, 3));
        // 启动客户端
        client.start();
        // 实例化注册发现工具
        JsonInstanceSerializer<ServiceInfo> serializer = new JsonInstanceSerializer<>(ServiceInfo.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                .client(client)
                .serializer(serializer)
                .basePath(REGISTRY_PATH)
                .build();
        this.serviceDiscovery.start();
        // 实例化负载均衡算法 直接写死，因为这里只有一个
        this.loadBalance = new RandomLoadBalance();
    }

    @Override
    public void register(ServiceInfo serviceInfo) throws Exception {
        System.out.println("begin registry serviceInfo to zookeeper server:     "+serviceInfo.getServiceName());
        //将服务端元数据保存到注册中心上
        ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo>builder()
                .name(serviceInfo.getServiceName())
                .address(serviceInfo.getServiceAddress())
                .port(serviceInfo.getServicePort())
                .payload(serviceInfo)
                .build();
        //注册
        this.serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public ServiceInfo discovery(String serviceName) throws Exception {
        System.out.println("begin discovery serviceInfo from zookeeper server:      "+serviceName);
        Collection<ServiceInstance<ServiceInfo>> serviceInstances = this.serviceDiscovery.queryForInstances(serviceName);
        //动态路由
        ServiceInstance<ServiceInfo> serviceInstance = this.loadBalance.select((List<ServiceInstance<ServiceInfo>>) serviceInstances);
        if (serviceInstance == null) {
            return null;
        }
        return serviceInstance.getPayload();
    }
}
