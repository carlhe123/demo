package com.carl.demo;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@EnableSwagger2Doc
@MapperScan("com.carl.demo.mapper")
//SpringBoot默认包扫描机制是：从启动类所在包开始，扫描当前包及其子包下的所有文件
@SpringBootApplication(scanBasePackages = {
		"com.carl.demo.controller",
		"com.carl.demo.service",
		"com.carl.demo.util",
		"com.carl.demo.config",
		"com.carl.demo.properties"
})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
