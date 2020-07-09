package com.carl.apollo.controller;

import cn.hutool.core.util.HexUtil;
import com.carl.apollo.mapper.DemoMapper;
import com.carl.util.encrypt.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.provider.MD5;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
public class TestController {

    @Value("${testValue}")
    private String testValue;
    @Value("${server.port}")
    private String port;
    @Value("${carl.testEnv}")
    private String testEnv;

    @Resource
    private DemoMapper demoMapper;

    @RequestMapping("/test")
    public String test(){
        String env = testEnv;
        String value = testValue;
        String port1 = port;
        System.out.println(port1 + "ENV:" + env);
        return value;
    }

    @RequestMapping("/mybatis-test")
    public String queryProductName(@RequestParam("id")Long id){
        String encrypt = MD5Util.MD5EncodeUtf8("123456");
        System.out.println(encrypt);
        BASE64Encoder encoder = new BASE64Encoder();
        String encodeString = encoder.encode("123456".getBytes());
        System.out.println(encodeString);
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] bytes = decoder.decodeBuffer(encodeString);
            String result = new String(bytes);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String productName = demoMapper.queryProductNameById(id);
        return productName;
    }
}
