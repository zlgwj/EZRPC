package com.zlgewj.service;

import com.zlgewj.pojo.User;

import java.util.List;

/**
 * @author zlgewj
 * @version 1.0

 */
public interface HelloService {
    String sayHello(String name);

    List<User> getFriends(User user);
}
