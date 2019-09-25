package com.jeffrey.springboot.testrediscache.testrediscache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisProperties {
    private int redisPort;
    private String redisHost;

    public RedisProperties(
            @Value("${redis.port}") int redisPort,
            @Value("${redis.host}") String redisHost) {
        this.redisPort = redisPort;
        this.redisHost = redisHost;
    }

    public int getRedisPort() {
        return this.redisPort;
    }

    public String getRedisHost() {
        return this.redisHost;
    }
}