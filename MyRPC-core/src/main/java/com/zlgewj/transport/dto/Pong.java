package com.zlgewj.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zlgewj
 * @version 1.0

 */
@AllArgsConstructor
@Builder
@Data
@ToString
public class Pong extends Message implements Serializable {
    private static final Integer type = 4;
    private final String name = "pong";

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getRequestId() {
        return 0;
    }
}
