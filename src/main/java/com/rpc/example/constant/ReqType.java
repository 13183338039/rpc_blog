package com.rpc.example.constant;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName ReqType.java
 * @Description TODO
 * @createTime 2022年04月23日 20:27:00
 */
public enum ReqType {

    REQUEST((byte) 1),
    RESPONSE((byte) 2),
    HEARTBEAT((byte) 3);


    private byte code;

    ReqType(byte code) {
        this.code = code;
    }

    public byte code() {
        return this.code;
    }

    public static ReqType findByCode(int code) {
        for (ReqType value : ReqType.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;
    }
}
