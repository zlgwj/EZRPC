package com.zlgewj.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zlgewj
 * @version 1.0

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
