package com.jeffrey.springboot.testrediscache.testrediscache.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("CacheEntity")
public class CacheEntity {

    @Id
    public String endpoint;

    public String responseBody;

    protected CacheEntity() {} // must declared this empty constructor otherwise Jackson databind will complain

    public CacheEntity(String endpoint, String responseBody) {
        this.endpoint = endpoint;
        this.responseBody = responseBody;
    }

}
