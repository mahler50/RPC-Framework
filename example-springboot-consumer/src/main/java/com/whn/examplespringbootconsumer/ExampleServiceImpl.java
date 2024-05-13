package com.whn.examplespringbootconsumer;

import com.whn.example.common.model.User;
import com.whn.example.common.service.UserService;
import com.whn.guazirpc.springboot.strater.annotation.RpcReference;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("kunkun");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }
}
