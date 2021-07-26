# SpringBoot整合

## 步骤

1. 新建SpringBoot项目，勾选web和nosql的redis

2. 查看依赖

   ```xml
   <dependencies>
       <!-- redis -->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-data-redis</artifactId>
       </dependency>
   
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
   
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-test</artifactId>
           <scope>test</scope>
       </dependency>
   
   </dependencies>
   ```

   点开redis的依赖，发现没有jedis，原因是jedis底层采用的直连，多线程操作是不安全的。

   目前采用的是lettuce，采用的是netty，不存在线程不安全情况，减少线程数量。

3. 配置Redis

## 更换使用Jedis

> 我在使用Redis做缓存的时候, 进行压力测试发现netty会报内存泄漏的异常, 这个时候就需要使用Jedis作连接, 同时jedis的线程不安全要加锁解决.

打开`spring-boot-starter-data-redis`的pom, 拉到最下面, 发现用得是lettuce的连接, 正是这个连接导致内存泄漏, 我们要更换他.

新的`pom.xm`

```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-redis</artifactId>
      <exclusions>
        <exclusion>
          <groupId>io.lettuce</groupId>
          <artifactId>lettuce-core</artifactId>
        </exclusion>
      </exclusions>
    </dependency>

    <dependency>
      <groupId>redis.clients</groupId>
      <artifactId>jedis</artifactId>
    </dependency>
```

查看父项目pom, 对jedis做了版本控制, 所以就不用指定版本了.

这个时候就解决了这个内存泄露的异常

具体[加锁](../notebooks/13.md)请参考.

