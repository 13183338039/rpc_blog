package com.rpc.example.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.rpc.example.constant.SerialType;
import com.rpc.example.core.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoSerializer implements ISerializer {

    // 每个线程一个 Kryo 实例，避免线程安全问题
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false); // 不要求注册类
        kryo.setClassLoader(Thread.currentThread().getContextClassLoader());
        return kryo;
    });

    @Override
    public <T> byte[] serializer(T obj) {
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        try {
            kryo.writeClassAndObject(output, obj);
            output.close(); // 自动 flush
            //RpcResponse response =deserializer()
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public <T> T deserializer(byte[] data, Class<T> clazz) {
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        Input input = new Input(bis);
        try {
            Object obj = kryo.readClassAndObject(input);
            return clazz.cast(obj); // 类型转换
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            input.close();
        }
        return null;
    }

    @Override
    public byte getType() {
        return SerialType.KRYO_SERIAL.code(); // 请确保你有定义 KRYO_SERIAL 类型
    }
}

