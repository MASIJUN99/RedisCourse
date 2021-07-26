package com.example.demo.controller;

import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountDownLatchController {

  @Autowired
  RedissonClient redissonClient;

  @RequestMapping("/lock")
  public String lock() throws InterruptedException {
    RCountDownLatch lock = redissonClient.getCountDownLatch("lock");
    lock.trySetCount(5);
    lock.await();
    return "success";
  }

  @RequestMapping("/get")
  public String get() {
    RCountDownLatch lock = redissonClient.getCountDownLatch("lock");
    lock.countDown();

    return "get one";

  }

}
