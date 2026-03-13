package com.rpc.example.handler;

import com.rpc.example.codec.RpcDecoder;
import com.rpc.example.codec.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcClientInitializer.java
 * @Description TODO
 * @createTime 2022年04月23日 21:37:00
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("RpcClientInitializer initChannel");
        ch.pipeline().addLast(
                new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                        12,
                        4,
                        0, 0))
                .addLast(new LoggingHandler()) // 日志处理
                //.addLast(new RpcClientHandler1())
                .addLast(new RpcEncoder())
                //.addLast(new RpcClientHandler1())
                .addLast(new RpcDecoder())
                .addLast(new RpcClientHandler());
        System.out.println("RpcClientInitializer initChannel --over ");
    }
}
