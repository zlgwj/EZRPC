package com.zlgewj.service;

import com.zlgewj.pojo.User;

import java.util.List;

/**
 * @author zlgewj
 * @version 1.0
 * @Date 2023/7/31 15:55
 */
public interface HelloService {
    String sayHello(String name);

    List<User> getFriends(User user);
}
