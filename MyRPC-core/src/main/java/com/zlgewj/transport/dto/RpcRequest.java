package com.zlgewj.transport.dto;

import com.zlgewj.serialize.Serializer;
import lombok.*;

import java.io.PipedReader;
import java.io.Serializable;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 19:58
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class RpcRequest extends Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Integer type = 1;
    private int requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private String version;
    private String group;

    @Override
    public int getType() {
        return type;
    }

    public String getRpcServiceName() {
        return this.interfaceName + "_" + this.getVersion();
    }
}
