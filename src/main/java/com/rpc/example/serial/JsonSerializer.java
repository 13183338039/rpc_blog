package com.rpc.example.serial;

import com.alibaba.fastjson.JSON;
import com.rpc.example.constant.SerialType;

import java.nio.charset.StandardCharsets;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName JsonSerializer.java
 * @Description TODO
 * @createTime 2022年04月23日 20:34:00
 */
public class JsonSerializer implements ISerializer {

    @Override
    public <T> byte[] serializer(T obj) {
        return JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserializer(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data), clazz);
    }

    @Override
    public byte getType() {
        return SerialType.JSON_SERIAL.code();
    }
}
