package com.example.demo.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/RedissonLock")
public class RedissonLockController {

    @Resource
    RedissonClient redissonClient;

    @RequestMapping("/write")
    public String write() {
        // use the lock from redisson
        String lockName = "redissonLock";
        RLock lock = redissonClient.getLock(lockName);
        // get lock
        lock.lock();
        try {
            // execute your transaction
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // unlock at the end
            lock.unlock();
        }
        return "success";
    }
}
