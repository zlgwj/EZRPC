package com.zlgewj.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/31 22:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage extends Message{

    private int requestId;
    private String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    @Override
    public int getType() {
        return 0;
    }
}
