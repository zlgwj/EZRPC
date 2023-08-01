package com.zlgewj.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author zlgewj
 * @version 1.0

 */
@AllArgsConstructor
@Builder
@Data
@ToString
public class Ping extends Message{

    private static final Integer type = 3;
    private final String name = "ping";
    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getRequestId() {
        return 0;
    }
}
