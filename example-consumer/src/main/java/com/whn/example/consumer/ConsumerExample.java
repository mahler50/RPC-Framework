package com.whn.example.consumer;

import com.whn.example.common.model.User;
import com.whn.example.common.service.UserService;
import com.whn.guazirpc.config.RpcConfig;
import com.whn.guazirpc.proxy.ServiceProxyFactory;
import com.whn.guazirpc.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("whn");

        // 调用服务
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }

        short number = userService.getNumber();
        System.out.println(number);
    }
}
