package com.mfs.sms.config.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {


    @Bean
    @Autowired
    public RedisTemplate<String,Object> redisTemplateConfig(RedisTemplate redisTemplate) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> hashValueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(hashValueSerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(hashValueSerializer);
        return redisTemplate;
    }
}
