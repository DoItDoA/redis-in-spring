package com.redis_in_spring.service;

import com.redis_in_spring.common.redis.RedisCommon;
import com.redis_in_spring.domain.hash.model.HashModel;
import com.redis_in_spring.domain.hash.response.HashResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashService {
    final private RedisCommon redis;

    public void putInHash(String key, String field, String name) {
        HashModel model = new HashModel(name);
        redis.putInHash(key, field, model);
    }

    public HashResponse getFromHash(String key, String field) {
        HashModel res = redis.getFromHash(key, field, HashModel.class);
        return new HashResponse(res);
    }
}
