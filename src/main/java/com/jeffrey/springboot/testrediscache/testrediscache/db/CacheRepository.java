package com.jeffrey.springboot.testrediscache.testrediscache.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheRepository extends CrudRepository<CacheEntity, String> {
}
