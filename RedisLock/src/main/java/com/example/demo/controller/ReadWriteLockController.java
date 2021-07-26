package com.example.demo.controller;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


/**
 * i suggest you try these situation:
 * <p/>
 * 1. read while write.
 * <p/>
 * 2. read then write and read.
 * <p/>
 * 3. write and multi read.
 * <p/>
 * @author Masijun
 */

@RestController
@RequestMapping("/ReadWriteLock")
public class ReadWriteLockController {

    @Autowired
    RedissonClient redissonClient;
    @Autowired
    RedisTemplate<String, String> redisTemplate;


    @RequestMapping("/write")
    public String write() {
        // random text, i used uuid there
        String uuid = UUID.randomUUID().toString();
        // got that read-write-lock
        String lockName = "rw-lock";
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(lockName);
        // i got the write-lock here
        RLock rLock = readWriteLock.writeLock();

        // lock in this thread or wait others thread unlock
        rLock.lock();
        try {
            // do something which write in database
            // here i set the random text in redis
            redisTemplate.opsForValue().set("text", uuid);
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // unlock in the end
            rLock.unlock();
        }
        return uuid;
    }

    @RequestMapping("/read")
    public String read() {
        String uuid = null;
        // we need use the same lock name if we want to lock this thread
        String lockName = "rw-lock";
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(lockName);
        // i got read-lock here
        RLock rLock = readWriteLock.readLock();

        rLock.lock();
        try {
            // do something which write in database
            // here i get the random text in redis
            uuid = redisTemplate.opsForValue().get("text");
            // Thread.sleep(10000);  // what will happen if i wanna write when i am reading?
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // unlock in the end
            rLock.unlock();
        }

        return uuid;
    }




}
