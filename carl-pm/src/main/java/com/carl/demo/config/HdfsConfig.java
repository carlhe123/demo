package com.carl.demo.config;

import com.carl.demo.bean.HdfsFile;
import com.carl.demo.properties.HdfsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author Carl.he
 * @Description Hadoop Hdfs配置文件
 * @Date 2020/1/14 10:01
 **/
@Configuration
@EnableConfigurationProperties(HdfsProperties.class)
public class HdfsConfig {

    @Resource
    private HdfsProperties hdfsProperties;

    public HdfsFile getHdfsFile(){
        return hdfsProperties.getHdfsFile();
    }
}
