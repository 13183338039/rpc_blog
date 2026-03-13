package com.rpc.example.handler;

import com.rpc.example.constant.ReqType;
import com.rpc.example.core.Header;
import com.rpc.example.core.RpcProtocol;
import com.rpc.example.core.RpcRequest;
import com.rpc.example.core.RpcResponse;
import com.rpc.example.spring.SpringBeanManager;
import com.rpc.example.spring.service.Mediator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
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
public class RpcServerHandler3 extends ChannelOutboundHandlerAdapter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // 可以选择性处理某些类型
        System.out.println("RpcServerHandler3 channelRead0" + LocalDateTime.now().format(formatter));
        System.out.println("RpcServerHandler3 channelRead0  --over" + LocalDateTime.now().format(formatter));
        // 继续传递
        super.write(ctx, msg, promise);
    }
}
