package com.redis_in_spring.service;

import com.redis_in_spring.common.redis.RedisCommon;
import com.redis_in_spring.domain.string.model.StringModel;
import com.redis_in_spring.domain.string.request.MultiStringRequest;
import com.redis_in_spring.domain.string.request.StringRequest;
import com.redis_in_spring.domain.string.response.StringResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StringService {
    private final RedisCommon redis;

    public void set(StringRequest req) {
        String key = req.baseRequest().key();
        StringModel model = new StringModel(key, req.name());

        redis.setData(key, model);
    }

    public StringResponse get(String key) {
        StringModel result = redis.getData(key, StringModel.class);

        List<StringModel> res = new ArrayList<>();
        if (result != null) {
            res.add(result);
        }
        return new StringResponse(res);
    }


    public void multiSet(MultiStringRequest req) {
        Map<String, StringModel> dataMap = new HashMap<>();

        for (int i = 0; i < req.names().length; i++) {
            String name = req.names()[i];
            String key = req.baseRequest().key() + ":" + (i + 1);
            StringModel model = new StringModel(key, name);
            dataMap.put(key, model);
        }
        redis.multiSetData(dataMap);
    }

    public StringResponse multiGet(String key) {
        List<StringModel> res = redis.multiGetData(key, StringModel.class);
        return new StringResponse(res);
    }

}
