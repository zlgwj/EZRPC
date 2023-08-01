package com.zlgewj.enums;

import cn.hutool.core.util.StrUtil;
import com.sun.deploy.util.StringUtils;
import com.zlgewj.exception.SerializerException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author zlgewj
 * @version 1.0

 */
@AllArgsConstructor
@Getter
public enum SerializeTypeEnum {

    KRYO((byte) 0x01,"kryo"),
    JAVA((byte) 0x02,"java");

    private final byte code;
    private final String name;

    public static SerializeTypeEnum getByName(String name) throws SerializerException {
        if (StrUtil.isAllBlank(name)) {
            throw new SerializerException("找不到序列化方式["+name+"]");
        }
        for (SerializeTypeEnum item : SerializeTypeEnum.values()) {
            if (name.equals(item.name)) {
                return item;
            }
        }
        throw new SerializerException("找不到序列化方式["+name+"]");
    }
}
