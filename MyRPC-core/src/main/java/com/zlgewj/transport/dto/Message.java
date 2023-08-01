package com.zlgewj.transport.dto;

import lombok.Data;

import java.util.HashMap;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 18:54
 */
@Data
public abstract class Message {
    private static final HashMap<Integer, Class<? extends Message>> MAP = new HashMap<>();
    static {
        MAP.put(1, RpcRequest.class);
        MAP.put(2, RpcResponse.class);
        MAP.put(3, Ping.class);
        MAP.put(4, Pong.class);
        MAP.put(0,ErrorMessage.class);
    }
    private String id;

    public abstract int getType();

    public abstract int getRequestId();

    public static Class<? extends Message> getMessageType(int i) {
        return MAP.get(i);
    }


}
