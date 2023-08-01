package com.zlgewj.transport.client;

import com.zlgewj.transport.dto.ErrorMessage;
import com.zlgewj.transport.dto.RpcResponse;
import io.netty.util.concurrent.Promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zlgewj
 * @version 1.0

 */
public class ResponseContainer {
    private static final Map<Integer, Promise<RpcResponse>> RESPONSE_MAP = new ConcurrentHashMap<>();

    public void put(int requestId, Promise<RpcResponse> responsePromise) {
        RESPONSE_MAP.put(requestId, responsePromise);
    }

    public void complete( RpcResponse response) {
        Promise<RpcResponse> rpcResponsePromise = RESPONSE_MAP.remove(response.getRequestId());
        rpcResponsePromise.setSuccess(response);
    }

    public void error(ErrorMessage message) {
        int requestId = message.getRequestId();
        RESPONSE_MAP.remove(requestId);
    }
}
