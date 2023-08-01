package com.zlgewj.annotation;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {
    String version() default "1.0.0";
    String group() default "default";
    String serviceName() default "";
}
