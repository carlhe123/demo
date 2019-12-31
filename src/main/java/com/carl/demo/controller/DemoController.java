package com.carl.demo.controller;

import com.carl.demo.bean.Result;
import com.carl.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author carl.he
 * @Date 2019/12/13
 **/
@RestController
@Slf4j
@RequestMapping("/demo")
public class DemoController {

    //@Slf4j 代替
//    private static final Logger log = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Resource
    private DemoService demoService;

    @RequestMapping(value = "/queryTest",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Result queryTest(@RequestParam(value = "id") Long id){
        log.info("测试参数："+id);
        String productName = demoService.queryProductNameById(id);
        log.info("返回结果："+productName);
        return Result.ok();
    }
}
