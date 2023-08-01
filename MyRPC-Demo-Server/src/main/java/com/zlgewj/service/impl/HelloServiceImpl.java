package com.zlgewj.service.impl;

import com.zlgewj.annotation.RpcService;
import com.zlgewj.pojo.User;
import com.zlgewj.service.HelloService;

import java.util.List;

/**
 * @author zlgewj
 * @version 1.0

 */
@RpcService(name = "hello")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello,"+name + "!!!!!!";
    }

    @Override
    public List<User> getFriends(User user) {
        int a = 1/0;
        return user.getFriends();
    }
}
