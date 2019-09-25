package com.jeffrey.springboot.testrediscache.testrediscache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.net.URISyntaxException;

@Configuration
public class LocalRedisConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalRedisConfiguration.class);

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//        JedisConnectionFactory jedisConFactory
//                = new JedisConnectionFactory();
//        jedisConFactory.setHostName("localhost");
//        jedisConFactory.setPort(6379);
//        return jedisConFactory;
//    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Bean(initMethod = "start", destroyMethod = "stop")
    RedisServer redisServerBuilder() {
        return RedisServer.builder()
                .port(redisPort)
                //.redisExecProvider(customRedisExec) //com.github.kstyrc (not com.orange.redis-embedded)
                .setting("maxmemory 128M") //maxheap 128M
                .build();
    }

}
