package com.carl.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableApolloConfig
@MapperScan(basePackages = {"com.carl.apollo.mapper"})
public class CarlApolloApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarlApolloApplication.class, args);
    }

}
