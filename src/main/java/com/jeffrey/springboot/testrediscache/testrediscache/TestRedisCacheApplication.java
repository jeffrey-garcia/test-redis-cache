package com.jeffrey.springboot.testrediscache.testrediscache;

import com.jeffrey.springboot.testrediscache.testrediscache.db.CacheEntity;
import com.jeffrey.springboot.testrediscache.testrediscache.db.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Date;

@SpringBootApplication
public class TestRedisCacheApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestRedisCacheApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TestRedisCacheApplication.class, args);
	}

	@Autowired
	Environment environment;

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

			RestTemplate restTemplate = rest();
			String port = environment.getProperty("local.server.port");

			while(true) {
				for (int i=0;i<5; i++) {
					ResponseEntity<?> responseEntity = restTemplate.getForEntity(URI.create("http://localhost:" + port + "/cache/refresh"), String.class);
					LOGGER.info("response: {}", responseEntity.getBody().toString());
				}
				LOGGER.info("waiting for next invocation cycle");
				Thread.sleep(60000);
			}
		};
	}

}
