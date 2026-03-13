package com.rpc.example.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName Header.java
 * @Description TODO
 * @createTime 2022年04月23日 20:09:00
 */
@Data
@AllArgsConstructor
public class Header implements Serializable {

    private short magic; // 魔数 2个字节

    private byte serialType; //序列化类型 1个字节

    private byte reqType; // 消息类型 1个字节

    private long requestId; // 请求ID 8个字节

    private int length; //消息体长度 4个字节
}
