package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.UUID;

@RestController
@RequestMapping("/SimpleLock")
public class SimpleLockController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @RequestMapping("/write")
    public String write() {
        // set a lock which value is random uuid
        String lockName = "simpleLock";
        String uuid = UUID.randomUUID().toString();
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockName, uuid);
        // if successfully set a 'lock', it means we got that lock.
        if (locked) {
            try {
                // execute your transaction
                System.out.println("I got that lock!");
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // use lua script to unlock, ensure unlock is atomic
                String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1]" +
                        "then" +
                        "return redis.call('del', KEYS[1])" +
                        "else" +
                        "return 0" +
                        "end";
                redisTemplate.execute(new DefaultRedisScript<>(luaScript, Long.class), Arrays.asList(lockName), uuid);
            }
        }
        return uuid;
    }
}
