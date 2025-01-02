package com.redis_in_spring.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.redis_in_spring.domain.strategy.model.ValueWithTTL;
import com.redis_in_spring.domain.string.model.StringModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCommon {
    private final RedisTemplate<String, String> template;

    @Value("${spring.data.redis.default-time}")
    private Duration defaultExpireTime;

    public <T> void setData(String key, T value) {
        template.opsForValue().set(key, this.valueToJson(value));
    }

    public <T> T getData(String key, Class<T> clazz) {
        String jsonValue = template.opsForValue().get(key);
        if (jsonValue == null) return null;

        return jsonToValue(jsonValue, clazz);
    }

    public <T> void multiSetData(Map<String, T> datas) {
        Map<String, String> jsonMap = new HashMap<>();
        for (Map.Entry<String, T> entry : datas.entrySet()) {
            T value = entry.getValue();
            jsonMap.put(entry.getKey(), this.valueToJson(value));
        }
        template.opsForValue().multiSet(jsonMap);
    }

    public <T> List<T> multiGetData(String key, Class<T> clazz) {
        Set<String> keys = template.keys(key + ":*");
        if (keys.isEmpty()) return null;
        List<String> jsonValues = template.opsForValue().multiGet(keys);

        List<T> list = new ArrayList<>();
        for (String jsonValue : jsonValues) {
            list.add(jsonToValue(jsonValue, clazz));
        }
        return list;
    }

    public <T> void addToZSet(String key, T value, double score) {
        template.opsForZSet().add(key, this.valueToJson(value), score);
    }

    public <T> List<T> rangeByScore(String key, double minScore, double maxScore, Class<T> clazz) {
        Set<String> jsonValues = template.opsForZSet().rangeByScore(key, minScore, maxScore); // score 오름차순
        List<T> resultSet = new ArrayList<>();
        if (jsonValues != null) {
            for (String jsonValue : jsonValues) {
                T v = this.jsonToValue(jsonValue, clazz);
                resultSet.add(v);
            }
        }
        return resultSet;
    }

    public <T> List<T> getTopNFromSortedSet(String key, int n, Class<T> clazz) {
        Set<String> jsonValues = template.opsForZSet().reverseRange(key, 0, n - 1); // score 내림차순
        List<T> resultSet = new ArrayList<>();
        if (jsonValues != null) {
            for (String jsonValue : jsonValues) {
                T v = this.jsonToValue(jsonValue, clazz);
                resultSet.add(v);
            }
        }
        return resultSet;
    }

    public <T> void addToListLeft(String key, T value) {
        String jsonValue = this.valueToJson(value);
        template.opsForList().leftPush(key, jsonValue);
    }

    public <T> void addToListRight(String key, T value) {
        String jsonValue = this.valueToJson(value);
        template.opsForList().rightPush(key, jsonValue);
    }

    public <T> List<T> getAllList(String key, Class<T> clazz) {
        List<String> jsonValues = template.opsForList().range(key, 0, -1);
        List<T> resultSet = new ArrayList<>();

        if (jsonValues != null) {
            for (String jsonValue : jsonValues) {
                T value = this.jsonToValue(jsonValue, clazz);
                resultSet.add(value);
            }
        }
        return resultSet;
    }

    public <T> void removeFromList(String key, T value) {
        String jsonValue = this.valueToJson(value);
        template.opsForList().remove(key, 1, jsonValue);
    }

    public <T> void putInHash(String key, String field, T value) {
        String jsonValue = this.valueToJson(value);
        template.opsForHash().put(key, field, jsonValue);
    }

    public <T> T getFromHash(String key, String field, Class<T> clazz) {
        Object result = template.opsForHash().get(key, field);
        if (result != null) {
            return jsonToValue(result.toString(), clazz);
        }
        return null;
    }

    public void removeFromHash(String key, String field) {
        template.opsForHash().delete(key, field);

    }

    public void setBit(String key, long offset, boolean value) {
        template.opsForValue().setBit(key, offset, value);
    }

    public boolean setBit(String key, long offset) {
        return template.opsForValue().getBit(key, offset);
    }

    public <T> ValueWithTTL<T> getValueWithTTL(String key, Class<T> clazz) {
        T value = null;
        Long ttl = null;

        try {
            List<Object> results = template.executePipelined((RedisCallback<Object>) connection -> {
                StringRedisConnection conn = (StringRedisConnection) connection;
                conn.get(key);
                conn.ttl(key);

                return null;
            });

            value = (T) this.jsonToValue((String) results.get(0), clazz);
            ttl = (Long) results.get(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ValueWithTTL<>(value, ttl);
    }

    public Long sumTwoKeyAndRenew(String key1, String key2, String resultKey) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();

        redisScript.setLocation(new ClassPathResource("/lua/newKey.lua"));
        redisScript.setResultType(Long.class);

        List<String> keys = Arrays.asList(key1, key2, resultKey);
        return template.execute(redisScript, keys);
    }

    private <T> String valueToJson(T value) {
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>((Class<T>) value.getClass());
        byte[] jsonBytes = serializer.serialize(value);
        return new String(jsonBytes, StandardCharsets.UTF_8);
    }

    private <T> T jsonToValue(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule()); // 생성자 기반 역직렬화 지원

        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, clazz);
        return serializer.deserialize(json.getBytes());
    }
}
