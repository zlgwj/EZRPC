package com.zlgewj.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {
    String version() default "1.0.0";
    String group() default "default";
    String name() default "";
}
