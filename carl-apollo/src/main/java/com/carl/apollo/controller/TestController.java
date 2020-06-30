package com.carl.apollo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${testValue}")
    private String testValue;
    @Value("${server.port}")
    private String port;
    @Value("${carl.testEnv}")
    private String testEnv;

    @RequestMapping("/test")
    public String test(){
        String env = testEnv;
        String value = testValue;
        String port1 = port;
        System.out.println(port1 + "ENV:" + env);
        return value;
    }
}
