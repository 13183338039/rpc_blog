package com.rpc.example;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RegistryType.java
 * @Description TODO
 * @createTime 2022年04月29日 20:26:00
 */
public enum RegistryType {

    ZOOKEEPER((byte) 0),
    EUREKA((byte) 1);

    private byte code;

    RegistryType(byte code) {
        this.code = code;
    }

    public static RegistryType findByCode(int code) {
        for (RegistryType value : RegistryType.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }

}
