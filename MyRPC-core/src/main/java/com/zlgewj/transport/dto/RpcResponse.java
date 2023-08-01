package com.zlgewj.transport.dto;

import com.zlgewj.enums.RpcResponseCodeEnum;
import lombok.*;

import java.io.Serializable;

/**
 * @author zlgewj
 * @version 1.0

 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcResponse extends Message implements Serializable {
    private static final long serialVersionUID = 2L;
    private int requestId;
    private Integer code;
    private String message;
    private static final Integer type = 2;
    private Object data;
    public static RpcResponse success(Object data, Integer requestId) {
        RpcResponse response = new RpcResponse();
        response.setCode(RpcResponseCodeEnum.SUCCESS.getCode());
        response.setMessage(RpcResponseCodeEnum.SUCCESS.getMsg());
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static RpcResponse fail(RpcResponseCodeEnum rpcResponseCodeEnum) {
        RpcResponse response = new RpcResponse();
        response.setCode(rpcResponseCodeEnum.getCode());
        response.setMessage(rpcResponseCodeEnum.getMsg());
        return response;
    }

    @Override
    public int getType() {
        return type;
    }
}
