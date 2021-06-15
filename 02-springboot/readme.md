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

