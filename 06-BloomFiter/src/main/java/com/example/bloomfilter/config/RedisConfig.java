package com.example.bloomfilter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {

  @Bean
  JedisPool jedisPool() {
    return new JedisPool("192.168.126.131", 6379);
  }

}
