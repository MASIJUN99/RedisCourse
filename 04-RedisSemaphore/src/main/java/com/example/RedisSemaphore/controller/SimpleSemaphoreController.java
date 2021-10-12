package com.example.RedisSemaphore.controller;

import javax.annotation.Resource;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleSemaphoreController {

  @Resource
  RedissonClient redissonClient;
  @Resource
  RedisTemplate<String, String> redisTemplate;

  @RequestMapping("/get")
  public String getOne() throws InterruptedException {
    RSemaphore park = redissonClient.getSemaphore("park");
    park.acquire();
    return "get resource";
  }

  @RequestMapping("/tryGet")
  public String tryGetOne() throws InterruptedException {
    RSemaphore park = redissonClient.getSemaphore("park");
    boolean b = park.tryAcquire();
    return b ? "get resource" : "fail to resource";
  }

  @RequestMapping("/set")
  public String setOne() {
    RSemaphore park = redissonClient.getSemaphore("park");
    park.release();
    return "set resource";
  }

}
