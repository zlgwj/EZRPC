package com.zlgewj.transport.client;

import com.zlgewj.transport.dto.RpcResponse;
import io.netty.util.concurrent.Promise;

import javax.xml.ws.Response;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 22:12
 */
public class ResponseContainer {
    private static final Map<Integer, Promise<RpcResponse<Object>>> RESPONSE_MAP = new ConcurrentHashMap<>();

    public void put(int requestId, Promise<RpcResponse<Object>> responsePromise) {
        RESPONSE_MAP.put(requestId, responsePromise);
    }

    public void complete( RpcResponse<Object> response) {
        Promise<RpcResponse<Object>> rpcResponsePromise = RESPONSE_MAP.remove(response.getRequestId());
        rpcResponsePromise.setSuccess(response);
    }
}
