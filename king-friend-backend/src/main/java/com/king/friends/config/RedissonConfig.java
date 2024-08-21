package com.king.friends.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName:RedissonConfig
 * Description:
 *
 * @Author:kinglearn
 * @Create2024/7/14 9:55
 * @version1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {
    private String host;
    private String port;

    @Bean
    public RedissonClient redisClint(){
        // 1.创建配置
        Config config = new Config();
//        创建地址字符串
        String redisAddress = String.format("redis://%s:%s",host,port);
//        String redisAddress="redis://192.168.8.177:6379";
//        设置地址和数据库
        config.useSingleServer().setAddress(redisAddress).setDatabase(3).setPassword("wangxue");
//        创建实例
         RedissonClient redissonClient = Redisson.create(config);
         return redissonClient;
    }
}
