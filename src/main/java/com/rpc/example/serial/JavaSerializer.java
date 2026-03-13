package com.rpc.example.serial;

import com.rpc.example.constant.SerialType;

import java.io.*;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName JavaSerializer.java
 * @Description TODO
 * @createTime 2022年04月23日 20:34:00
 */
public class JavaSerializer implements ISerializer {

    @Override
    public <T> byte[] serializer(T obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj); //序列化
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public <T> T deserializer(byte[] data, Class<T> clazz) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte getType() {
        return SerialType.JAVA_SERIAL.code();
    }
}
