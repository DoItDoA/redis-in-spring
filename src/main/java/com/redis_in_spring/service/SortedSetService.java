package com.redis_in_spring.service;

import com.redis_in_spring.common.redis.RedisCommon;
import com.redis_in_spring.domain.sortedSet.model.SortedSetModel;
import com.redis_in_spring.domain.sortedSet.request.SortedSetRequest;
import com.redis_in_spring.domain.sortedSet.response.SortedSetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SortedSetService {
    private final RedisCommon redis;

    public void setSortedSet(SortedSetRequest req) {
        SortedSetModel model = new SortedSetModel(req.name(), req.score());
        redis.addToZSet(req.baseRequest().key(), model, req.score());
    }

    public SortedSetResponse getDataByRange(String key, double min, double max) {
        List<SortedSetModel> res = redis.rangeByScore(key, min, max, SortedSetModel.class);
        return new SortedSetResponse(res);
    }

    public SortedSetResponse getTopN(String key, int n) {
        List<SortedSetModel> res = redis.getTopNFromSortedSet(key, n, SortedSetModel.class);
        return new SortedSetResponse(res);
    }
}
