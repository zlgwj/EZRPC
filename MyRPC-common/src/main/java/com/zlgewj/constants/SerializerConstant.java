package com.zlgewj.constants;

import java.nio.file.FileAlreadyExistsException;
import java.util.HashMap;

/**
 * @author zlgewj
 * @version 1.0

 */
public class SerializerConstant {
    private static final HashMap<Integer, String> map = new HashMap<>();
    static {
        map.put(1,"kryo");
        map.put(2,"java");
    }
    public static final String KRYO = "kryo";
    public static final String JAVA = "java";

    public static String getSerializeType(int i) {
        return map.get(i);
    }

}
