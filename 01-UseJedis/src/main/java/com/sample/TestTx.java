package com.sample;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestTx {

  public static void main(String[] args) {
    Jedis jedis = new Jedis("127.0.0.1", 6379);

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", "masijun");
    jsonObject.put("birth", "1997-10-03");
    String s = jsonObject.toJSONString();

    jedis.flushDB();
    // 开启事务
    Transaction multi = jedis.multi();

    try {
      multi.set("user", s);
      multi.exec();
    } catch (Exception e) {
      multi.discard();
      e.printStackTrace();
    } finally {
      System.out.println(jedis.get("user"));
    }

  }

}
