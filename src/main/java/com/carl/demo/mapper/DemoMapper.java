package com.carl.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: 测试Mapper
 * @Author carl.he
 * @Date 2019/12/12
 **/
//@Mapper
public interface DemoMapper {

    String queryProductNameById(Long id);

}
