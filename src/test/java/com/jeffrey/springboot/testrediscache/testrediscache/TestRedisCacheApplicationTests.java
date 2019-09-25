package com.jeffrey.springboot.testrediscache.testrediscache;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class TestRedisCacheApplicationTests {

//	@Test
//	public void contextLoads() {
//	}

	@Test
	public void test1() {
		String input = "count=5";
		String output = input.substring("count=".length(), input.length());
		Assert.assertTrue(true);
	}
}
