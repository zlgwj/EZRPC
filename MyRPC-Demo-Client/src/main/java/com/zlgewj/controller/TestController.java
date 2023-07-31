package com.zlgewj.controller;

import com.zlgewj.annotation.RpcReference;
import com.zlgewj.pojo.User;
import com.zlgewj.service.HelloService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/31 16:49
 */
@Component
public class TestController {

    @RpcReference(serviceName = "hello")
    HelloService helloService;

    public String test() {
        String s = helloService.sayHello("zhangsan");
        return s;
    }

    public void test2() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(2L,"lisi",22,null));
        users.add(new User(3L,"zhangsan",12,null));
        User my = new User(1L, "wangwu", 21,users);
        List<User> friends = helloService.getFriends(my);
        System.out.println(friends);
    }
}
