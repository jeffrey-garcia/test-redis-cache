package com.jeffrey.springboot.testrediscache.testrediscache;

import com.jeffrey.springboot.testrediscache.testrediscache.db.CacheEntity;
import com.jeffrey.springboot.testrediscache.testrediscache.db.CacheRepository;
import com.jeffrey.springboot.testrediscache.testrediscache.db.CacheService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@SpringBootApplication
public class TestRedisCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestRedisCacheApplication.class, args);
	}

	@Bean
	public RestTemplate rest() {
		return new RestTemplateBuilder().build();
	}

	@Bean
	public CommandLineRunner insertDataToCache(CacheService cacheService) {
		return (args) -> {
			cacheService.deleteAll();
			cacheService.save(new CacheEntity("test1-key", new Date().toString()));
			cacheService.save(new CacheEntity("test2-key", new Date().toString()));
			cacheService.save(new CacheEntity("test3-key", new Date().toString()));
			cacheService.save(new CacheEntity("test4-key", new Date().toString()));
		};
	}

}
