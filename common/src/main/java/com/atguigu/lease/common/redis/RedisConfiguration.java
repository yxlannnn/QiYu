package com.atguigu.lease.common.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;


//对redis进行配置的类
//利用这个类和redis进行交互
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> stringObjectRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        //new一个自定义的redisTemplate，健是String值是Object
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //设置redis连接工厂为redisConnectionFactory
        template.setConnectionFactory(redisConnectionFactory);
        //设置键的序列化器为string
        template.setKeySerializer(RedisSerializer.string());
        //设置值的序列化器为java
        template.setValueSerializer(RedisSerializer.java());

        return template;
    }
}