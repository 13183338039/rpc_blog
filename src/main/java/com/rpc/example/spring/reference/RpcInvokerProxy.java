package com.rpc.example.spring.reference;

import com.alibaba.fastjson.JSONObject;
import com.rpc.example.IRegistryService;
import com.rpc.example.constant.ReqType;
import com.rpc.example.constant.RpcConstant;
import com.rpc.example.constant.SerialType;
import com.rpc.example.core.*;
import com.rpc.example.protocol.NettyClient;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcInvokerProxy.java
 * @Description TODO
 * @createTime 2022年04月23日 21:59:00
 */
public class RpcInvokerProxy implements InvocationHandler {

    // 不需要手动传入了，直接从注册中心获取
    private IRegistryService registryService;

//    private String host;
//
//    private int port;
//
//    public RpcInvokerProxy(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }

    @PostConstruct
    public void init() {
        System.out.println("RpcInvokerProxy" + " _init");
    }
    public RpcInvokerProxy(IRegistryService registryService) {
        System.out.println("动态代理类 设置 注册中心");
        this.registryService = registryService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("动态代理 --->  反射执行");
        RpcProtocol<RpcRequest> reqProtocol = new RpcProtocol<>();
        long requestId = RequestHolder.REQUEST_ID.incrementAndGet();
        Header header = new Header(RpcConstant.MAGIC, SerialType.JSON_SERIAL.code(),
                ReqType.REQUEST.code(), requestId, 0);
        reqProtocol.setHeader(header);

        RpcRequest request = new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParamsTypes(method.getParameterTypes());
        request.setParams(args);
        reqProtocol.setContent(request);

        //短链接
//        NettyClient nettyClient = new NettyClient(host, port);
        // 不需要传递地址及端口了
        NettyClient nettyClient = new NettyClient();
        // 通过设置DefaultEventLoop进行轮询获取结果
        RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<RpcResponse>(new DefaultEventLoop()));
        // 保存请求ID和返回数据的对应关系
        RequestHolder.REQUEST_MAP.put(requestId, future);
        nettyClient.sendRequest(reqProtocol, registryService);
        // 返回异步回调的数据
        Object re=future.getPromise().get().getData();
        if(re!=null){
            System.out.println("远程服务执行完毕:   "+re.getClass().getName());
        }
        System.out.println("method:     "+method.getReturnType().toString());
        Class<?> returnParamtypes=method.getReturnType();
        if(re instanceof JSONObject){
            JSONObject t=(JSONObject) re;
            return t.toJavaObject(returnParamtypes);
        }
        return re;
//
//        Class<?>[] paramTypes = method.getParameterTypes(); // 获取目标方法参数类型
//        System.out.println("远程服务执行完毕:   "+re.getClass().getName());
//        Object[] realArgs = new Object[paramTypes.length];
//        for (int i = 0; i < paramTypes.length; i++) {
//            Class<?> paramType = paramTypes[i];
//            // 获取目标类型的参数名，假设 JSONObject 里的 key 和参数名一致
//            String paramName = paramType.getSimpleName().toLowerCase();
//
//            // 获取 raw 数据
//            JSONObject t=(JSONObject) re;
//            //Object raw = t.get(paramName);
//            realArgs[i]=t.toJavaObject(paramType);
//        }
//        // 返回转换后的参数数组
//        return realArgs[0];
        //return re;
    }
}
