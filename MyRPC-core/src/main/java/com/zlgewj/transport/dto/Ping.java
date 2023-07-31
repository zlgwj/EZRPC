package com.zlgewj.transport.dto;

import lombok.*;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/30 20:41
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
