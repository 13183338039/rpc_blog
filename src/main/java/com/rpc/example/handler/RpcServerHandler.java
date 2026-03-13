package com.rpc.example.handler;

import com.rpc.example.constant.ReqType;
import com.rpc.example.core.Header;
import com.rpc.example.core.RpcProtocol;
import com.rpc.example.core.RpcRequest;
import com.rpc.example.core.RpcResponse;
import com.rpc.example.spring.SpringBeanManager;
import com.rpc.example.spring.service.Mediator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcServerHandler.java
 * @Description 服务端事件处理器
 * @createTime 2022年04月23日 21:14:00
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg) throws Exception {
        System.out.println("RpcServerHandler channelRead0"+ LocalDateTime.now().format(formatter));
        RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();
        // 处理服务端返回对象
        Header header = msg.getHeader();
        header.setReqType(ReqType.RESPONSE.code());
        // 通过反射调用对应方法
//        Object result = invoke(msg.getContent());
        Object result = Mediator.getInstance().processor(msg.getContent());
        resProtocol.setHeader(header);
        RpcResponse response = new RpcResponse();
        response.setData(result);
        response.setMsg("success");
        resProtocol.setContent(response);

        ctx.writeAndFlush(resProtocol);
        System.out.println("RpcServerHandler channelRead0  --over"+ LocalDateTime.now().format(formatter));
    }

//    // 通过反射进行调用 废弃
//    @Deprecated
//    private Object invoke(RpcRequest request) {
//        try {
//            // 反射加载
//            Class<?> clazz = Class.forName(request.getClassName());
//            // 加载实例
//            Object bean = SpringBeanManager.getBean(clazz);
//            // 加载实例调用的方法
//            Method method = clazz.getDeclaredMethod(request.getMethodName(), request.getParamsTypes());
//            // 通过反射调用
//            return method.invoke(bean, request.getParams());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}

