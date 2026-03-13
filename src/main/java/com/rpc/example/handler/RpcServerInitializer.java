package com.rpc.example.handler;

import com.rpc.example.codec.RpcDecoder;
import com.rpc.example.codec.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcServerInitializer.java
 * @Description TODO
 * @createTime 2022年04月23日 21:13:00
 */
public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("RpcServerInitializer initChannel");
        ch.pipeline().
                addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                        12,
                        4,
                        0,
                        0))
                .addLast(new RpcDecoder())
                .addLast(new RpcServerHandler1())
                .addLast(new RpcEncoder())
                .addLast(new RpcServerHandler2())
                .addLast(new RpcServerHandler())
                .addLast(new RpcServerHandler3());
        System.out.println("RpcServerInitializer initChannel --over");
    }
}

