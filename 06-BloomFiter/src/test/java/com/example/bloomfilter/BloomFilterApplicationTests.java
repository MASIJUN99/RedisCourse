package com.example.bloomfilter;

import com.example.bloomfilter.redisbase.RedisBloomFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BloomFilterApplicationTests {

  @Autowired
  RedisBloomFilter redisBloomFilter;

  @Test
  void contextLoads() {
    redisBloomFilter.init(100, 0.01);
    redisBloomFilter.insert("test", "MSJ", 300 * 1000);
    redisBloomFilter.insert("test", "XJY", 300 * 1000);
    redisBloomFilter.insert("test", "LSZ", 300 * 1000);
    redisBloomFilter.insert("test", "CZQ", 300 * 1000);
    redisBloomFilter.insert("test", "WYL", 300 * 1000);
    redisBloomFilter.insert("test", "CYN", 300 * 1000);
    System.out.println(redisBloomFilter.contains("test", "CYN"));
    System.out.println(redisBloomFilter.contains("test", "JSL"));
  }

}
