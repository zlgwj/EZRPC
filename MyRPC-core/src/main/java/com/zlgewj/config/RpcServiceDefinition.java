package com.zlgewj.config;

import lombok.*;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/31 12:58
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RpcServiceDefinition {

    private String version = "";
    private String group = "";
    private String interfaceName = "";
    private Object service;

    public String getServiceName() {
        return interfaceName+"_"+version;
    }


}
