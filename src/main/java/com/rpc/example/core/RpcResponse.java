package com.rpc.example.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 陳樂
 * @version 1.0.0
 * @ClassName RpcResponse.java
 * @Description TODO
 * @createTime 2022年04月23日 20:25:00
 */
@Data
public class RpcResponse  implements Serializable {

    private Object data;

    private String msg;
}
