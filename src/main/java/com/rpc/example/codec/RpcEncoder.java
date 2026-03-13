package com.rpc.example.codec;

import com.rpc.example.core.Header;
import com.rpc.example.core.RpcProtocol;
import com.rpc.example.serial.ISerializer;
import com.rpc.example.serial.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcEncoder.java
 * @Description 编码处理器
 * @createTime 2022年04月23日 21:07:00
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, ByteBuf out) throws Exception {
        System.out.println("============begin RpcEncoder========="+ LocalDateTime.now().format(formatter));
        Header header = msg.getHeader();
        out.writeShort(header.getMagic());
        out.writeByte(header.getSerialType());
        out.writeByte(header.getReqType());
        out.writeLong(header.getRequestId());
        // 序列化内容
        ISerializer serializer = SerializerManager.getSerializer(header.getSerialType());
        byte[] data = serializer.serializer(msg.getContent());
        out.writeInt(data.length);
        out.writeBytes(data);
    }
}
