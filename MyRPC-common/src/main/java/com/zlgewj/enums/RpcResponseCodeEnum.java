package com.zlgewj.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Stack;

/**
 * @author zlgewj
 * @version 1.0

 */
@AllArgsConstructor
@ToString
@Getter
public enum RpcResponseCodeEnum {

    SUCCESS(1,"调用成功"),
    FAIL(2, "调用失败");

    private final Integer code;
    private final String msg;


}
