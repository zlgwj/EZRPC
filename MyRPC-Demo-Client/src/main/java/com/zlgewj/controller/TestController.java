package com.zlgewj.controller;

import com.zlgewj.annotation.RpcReference;
import com.zlgewj.service.HelloService;
import org.springframework.stereotype.Component;

/**
 * @author zlgewj
 * @version 1.0

 */
@Component
public class TestController {

    @RpcReference(serviceName = "hello")
    HelloService helloService;
    public String test() {
        String s = helloService.sayHello("zhangsan");
        return s;
    }
}
