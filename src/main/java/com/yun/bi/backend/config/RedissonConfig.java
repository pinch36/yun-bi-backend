package com.yun.bi.backend.config;

import lombok.Data;
import org.apache.xmlbeans.impl.xb.xmlconfig.ConfigDocument;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: __yun
 * @Date: 2024/08/26/9:24
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {
    private Integer database;
    private String host;
    private Integer port;
//    private String password;
    @Bean
    public RedissonClient getRedissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setDatabase(database)
//                .setPassword(password)
                .setAddress("redis://"+host+":"+port);
        return Redisson.create(config);
    }
}
