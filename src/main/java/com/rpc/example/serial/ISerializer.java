package com.rpc.example.serial;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName ISerializer.java
 * @Description 序列化接口
 * @createTime 2022年04月23日 20:31:00
 */
public interface ISerializer {

    /*
    序列化接口
     */
    <T> byte[] serializer(T obj);

    /*
    反序列化接口
     */
    <T> T deserializer(byte[] data, Class<T> clazz);

    /*
    序列化类型
     */
    byte getType();

}
