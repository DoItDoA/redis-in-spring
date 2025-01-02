package com.redis_in_spring.service;

import com.redis_in_spring.common.redis.RedisCommon;
import com.redis_in_spring.domain.list.model.ListModel;
import com.redis_in_spring.domain.list.response.ListResponse;
import com.redis_in_spring.domain.sortedSet.model.SortedSetModel;
import com.redis_in_spring.domain.sortedSet.request.SortedSetRequest;
import com.redis_in_spring.domain.sortedSet.response.SortedSetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListService {
    private final RedisCommon redis;

    public void addToListLeft(String key, String name) {
        ListModel model = new ListModel(name);
        redis.addToListLeft(key, model);
    }

    public void addToListRight(String key, String name) {
        ListModel model = new ListModel(name);
        redis.addToListRight(key, model);
    }

    public ListResponse getAllData(String key) {
        List<ListModel> res = redis.getAllList(key, ListModel.class);
        return new ListResponse(res);
    }
}
