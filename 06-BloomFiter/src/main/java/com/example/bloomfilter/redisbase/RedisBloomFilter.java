package com.example.bloomfilter.redisbase;

import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

@Component
public class RedisBloomFilter {

  public final static String RS_BF_NS = "rbf:";
  private int numApproxElements;
  private double fpp;  // 可接受的误差
  private int numHashFunction;
  private int bitmapLength;

  @Autowired
  private JedisPool jedisPool;

  /**
   * 初始化
   * @param numApproxElements 预估的数据量
   * @param fpp 可允许的误差范围
   */
  public RedisBloomFilter init(int numApproxElements, double fpp) {
    this.numApproxElements = numApproxElements;
    this.fpp = fpp;

    // 位数组长度
    this.bitmapLength = (int) (-numApproxElements * Math.log(fpp) / Math.pow(Math.log(2), 2));
    // 哈希函数个数
    this.numHashFunction = (int) (Math.log(2) * bitmapLength / numApproxElements);
    return this;
  }

  /**
   * 获得element的下标集合
   * @param element 元素值
   * @return 下标数组
   */
  private long[] getBitIndices(String element) {
    long[] indices = new long[numHashFunction];

    long hash64 = Hashing.murmur3_128()
        .hashObject(element, Funnels.stringFunnel(StandardCharsets.UTF_8))
        .asLong();
    int hash1 = (int) hash64;
    int hash2 = (int) (hash64 >> 32);

    int combinedHash = hash1;
    for (int i = 0; i < numHashFunction; i++) {
      indices[i] = (combinedHash & Integer.MAX_VALUE) % bitmapLength;  // 与一下，丢弃溢出位
      combinedHash += hash2;  // 模拟换了个哈希函数
    }
    return indices;
  }

  /**
   * 插入元素，类似put
   * @param key 需要插入的布隆过滤器
   * @param element 要插入的值
   * @param milleSecond 过期时间（毫秒）
   */
  public void insert(String key, String element, long milleSecond) {
    if (key == null || element == null) {
      throw new RuntimeException("键值不允许为空");
    }
    String actualKey = RS_BF_NS.concat(key);

    Jedis jedis = jedisPool.getResource();
    try {
      Pipeline pipelined = jedis.pipelined();
      for (long bitIndex : getBitIndices(element)) {
        pipelined.setbit(actualKey, bitIndex, true);
      }
      pipelined.syncAndReturnAll();
    } catch (Exception e) {
      e.printStackTrace();
    }
    jedis.expire(actualKey, milleSecond);
  }

  /**
   * 验证布隆过滤器是否含有某值
   * @param key 布隆过滤器
   * @param element 值
   * @return 是否含有
   */
  public boolean contains(String key, String element) {
    if (key == null || element == null) {
      throw new RuntimeException("键值不允许为空");
    }
    String actualKey = RS_BF_NS.concat(key);

    boolean res = true;
    Jedis jedis = jedisPool.getResource();
    try {
      Pipeline pipelined = jedis.pipelined();
      for (long bitIndex : getBitIndices(element)) {
        pipelined.getbit(actualKey, bitIndex);
      }
      res = !pipelined.syncAndReturnAll().contains(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }



}

