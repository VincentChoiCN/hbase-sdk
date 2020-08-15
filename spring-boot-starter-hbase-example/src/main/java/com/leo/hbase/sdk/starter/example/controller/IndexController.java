package com.leo.hbase.sdk.starter.example.controller;


import com.leo.hbase.sdk.starter.example.pojo.UserPojo;
import com.leo.hbase.sdk.starter.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>测试的一个controller</p>
 *
 * @author leo.jie (leojie1314@gmail.com)
 */
@RestController
public class IndexController {
    @Autowired
    private UserService userService;

    @GetMapping("/getUser")
    public UserPojo getUser() {
        return userService.getUser("10001");
    }
}
