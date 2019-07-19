package com.jeffrey.springboot.testrediscache.testrediscache;

import com.jeffrey.springboot.testrediscache.testrediscache.db.CacheEntity;
import com.jeffrey.springboot.testrediscache.testrediscache.db.CacheService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;

@RestController
public class TestRedisCacheController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestRedisCacheController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private Environment environment;

    @GetMapping(path="/cache/all")
    public @ResponseBody Iterable<CacheEntity> getAll() {
        return cacheService.findAll();
    }

    @GetMapping(path="/cache/count")
    public @ResponseBody long count() {
        return cacheService.count();
    }

    @PostMapping(path="/cache/update")
    public @ResponseBody CacheEntity updateCache(@RequestBody CacheEntity entity) {
        LOGGER.info("cache update: {}", entity.responseBody);
        /**
         * Sample curl request
         * curl -i -X POST "http://localhost:8080/cache/update" -H "accept: application/json" -H "Content-Type: application/json" -d "{\"endpoint\":\"test4-key\",\"responseBody\":\"12345\"}"
         */
        cacheService.update(entity);
        return entity;
    }

    @GetMapping(path="/cache/find")
    public @ResponseBody CacheEntity findCacheById(@RequestParam String endpoint) {
        return cacheService.findById(endpoint);
    }

    @GetMapping(path="/cache/refresh")
    public @ResponseBody String invokeCacheUpdate() {
        try {
            String authHeader = "Basic " + new String(Base64.encodeBase64("admin:password".getBytes(Charset.forName("US-ASCII"))));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//            params.add("name", "spring-cloud-task-redis-cache");
            params.add("name", "mytask");

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

//            URI uri = URI.create("http://localhost:9393/tasks/executions");
            URI uri = URI.create("https://test-spring-dataflow-server.apps.sea.preview.pcf.manulife.com/tasks/executions");

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            HttpStatus statusCode = responseEntity.getStatusCode();
            if (statusCode == HttpStatus.CREATED) {
                String result = responseEntity.getBody();
                LOGGER.info("TASK SUBMITTED, EXECUTION ID: {}", result);
                return result;
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        return "-1"; // refresh cache operation not initiated!
    }

    @RequestMapping(path="/cache/refreshBatch")
    public @ResponseBody ResponseEntity invokeBatchCacheUpdate(@RequestBody String concurrentTasksNumber) {
        LOGGER.info("invokeBatchCacheUpdate: {}", concurrentTasksNumber);
        String port = environment.getProperty("local.server.port");
        int count = Integer.parseInt(concurrentTasksNumber.substring("count=".length(), concurrentTasksNumber.length()));

        for (int i=0;i<count; i++) {
            ResponseEntity<?> responseEntity = restTemplate.getForEntity(URI.create("http://localhost:" + port + "/cache/refresh"), String.class);
            LOGGER.info("response: {}", responseEntity.getBody().toString());
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
