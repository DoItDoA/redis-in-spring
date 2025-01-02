package com.redis_in_spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching // Spring Boot의 캐싱 설정을 활성화
public class RedisCacheConfig {
    @Bean
    public CacheManager boardCacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 1. ObjectMapper 설정
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()) // Java 8 날짜 및 시간 지원
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO-8601 형식으로 날짜 직렬화

        // 2. Jackson2JsonRedisSerializer에 ObjectMapper 설정
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // Redis에 Key를 저장할 때 String으로 직렬화(변환)해서 저장
                // 직렬화 방식이 지정되지 않으면 바이너리 데이터가 저장이 된다. 즉 키 확인시 10110 같이 숫자로 확인이 된다.
                // StringRedisSerializer는 Java의 문자열 데이터를 UTF-8 형식으로 직렬화하여 Redis에 저장
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // Redis에 Value를 저장할 때 Json으로 직렬화(변환)해서 저장
                // Jackson2JsonRedisSerializer는 객체를 JSON 문자열로 변환하여 저장하고, JSON 문자열을 다시 객체로 역직렬화하는 데 사용
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                // 데이터의 만료기간(TTL) 설정 (1분)
                .entryTtl(Duration.ofMinutes(1L));

        return RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
