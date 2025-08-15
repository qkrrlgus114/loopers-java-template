package com.loopers.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    // defaultTyping 절대 켜지 마세요!
    private GenericJackson2JsonRedisSerializer genericSerializer() {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        // om.activateDefaultTyping(...) 금지
        return new GenericJackson2JsonRedisSerializer(om);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Object> t = new RedisTemplate<>();
        t.setConnectionFactory(cf);
        var s = genericSerializer();
        t.setKeySerializer(new StringRedisSerializer());
        t.setValueSerializer(s);
        t.setHashKeySerializer(new StringRedisSerializer());
        t.setHashValueSerializer(s);
        return t;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        var s = genericSerializer();
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(s));
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory cf, RedisCacheConfiguration cfg) {
        return RedisCacheManager.builder(cf).cacheDefaults(cfg).build();
    }
}
