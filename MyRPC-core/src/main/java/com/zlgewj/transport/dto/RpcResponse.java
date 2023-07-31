package com.zlgewj.transport.dto;

import com.zlgewj.enums.RpcResponseCodeEnum;
import lombok.*;

import java.io.Serializable;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 19:58
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcResponse<T> extends Message implements Serializable {
    private static final long serialVersionUID = 2L;
    private int requestId;
    private Integer code;
    private String message;
    private static final Integer type = 2;
    private T data;
    public static <T> RpcResponse<T> success(T data, Integer requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCodeEnum.SUCCESS.getCode());
        response.setMessage(RpcResponseCodeEnum.SUCCESS.getMsg());
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static <T> RpcResponse<T> fail(RpcResponseCodeEnum rpcResponseCodeEnum) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(rpcResponseCodeEnum.getCode());
        response.setMessage(rpcResponseCodeEnum.getMsg());
        return response;
    }

    @Override
    public int getType() {
        return type;
    }
}
