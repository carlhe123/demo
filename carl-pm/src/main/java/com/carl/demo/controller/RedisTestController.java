package com.carl.demo.controller;

import com.carl.demo.bean.Result;
import com.carl.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author carl.he
 * @Date 2019/12/16
 **/

@RestController
@Slf4j
@RequestMapping("/redis")
public class RedisTestController {

    @Resource
    private DemoService demoService;

    @RequestMapping(value = "/getProdNameById",method = RequestMethod.GET)
    public Result getProdNameById(@RequestParam(value = "id") Long id){
        String prodName = demoService.queryProductNameByIdCache(id);
        return Result.ok(prodName);
    }

    @RequestMapping(value = "/setProdName",method = RequestMethod.GET)
    public Result setProdName(@RequestParam(value = "id") Long id
            , @RequestParam(value = "productName") String productName){
        demoService.setProductName(id,productName);
        return Result.ok("保存数据成功");
    }
}
