package com.carl.demo;

import com.carl.demo.service.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Resource
	private DemoService demoService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void queryTest(){
		Long id = Long.valueOf(20000456);
		String productName = demoService.queryProductNameById(id);
		System.out.println(productName);
	}

}
