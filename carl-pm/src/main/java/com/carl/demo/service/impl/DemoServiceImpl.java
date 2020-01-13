package com.carl.demo.service.impl;

import com.carl.demo.mapper.DemoMapper;
import com.carl.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @Author carl.he
 * @Date 2019/12/13
 **/
@Component
public class DemoServiceImpl implements DemoService {

    @Resource
    private DemoMapper demoMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    static final String KEY_RESV_PRODUCT_NAME = "resv_prod_info_%s";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String queryProductNameById(Long id) {
        String productName = demoMapper.queryProductNameById(id);
        return productName;
    }

    @Override
    public String queryProductNameByIdCache(Long id) {
        String productName = stringRedisTemplate.opsForValue().get(String.format(KEY_RESV_PRODUCT_NAME, id));
        if (StringUtils.isEmpty(productName)) {
            // Redis中没有就读数据库
            productName = demoMapper.queryProductNameById(id);
        }
        return productName;
    }

    @Override
    public void setProductName(Long id, String productName){
        stringRedisTemplate.opsForValue().set(String.format(KEY_RESV_PRODUCT_NAME, id), productName);
    }
}
