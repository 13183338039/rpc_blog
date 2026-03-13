package com.rpc.example.handler;


import com.rpc.example.core.RequestHolder;
import com.rpc.example.core.RpcFuture;
import com.rpc.example.core.RpcProtocol;
import com.rpc.example.core.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcClientHandler.java
 * @Description 客户端事件处理器
 * @createTime 2022年04月23日 21:38:00
 */
public class RpcClientHandler1 extends ChannelOutboundHandlerAdapter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("RpcClientHandler1-channelRead0"+ LocalDateTime.now().format(formatter));
        System.out.println("RpcClientHandler1-channelRead0 --over");
    }
}

