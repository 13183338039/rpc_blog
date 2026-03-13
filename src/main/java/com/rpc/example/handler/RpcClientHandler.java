package com.rpc.example.handler;

import com.rpc.example.core.RequestHolder;
import com.rpc.example.core.RpcFuture;
import com.rpc.example.core.RpcProtocol;
import com.rpc.example.core.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
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
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg) throws Exception {
        System.out.println("RpcClientHandler-channelRead0"+ LocalDateTime.now().format(formatter));
        long requestId = msg.getHeader().getRequestId();
        //根据ID获得异步对象
        RpcFuture<RpcResponse> future = RequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(msg.getContent()); //返回结果
        System.out.println("RpcClientHandler-channelRead0 --over");
    }
}
