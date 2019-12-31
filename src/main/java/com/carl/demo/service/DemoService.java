package com.carl.demo.service;

/**
 * @Description: TODO
 * @Author carl.he
 * @Date 2019/12/13
 **/
public interface DemoService {

    String queryProductNameById(Long id);

    String queryProductNameByIdCache(Long id);

    void setProductName(Long id, String productName);
}
