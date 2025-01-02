package com.redis_in_spring.domain.list.response;


import com.redis_in_spring.domain.list.model.ListModel;

import java.util.List;

public record ListResponse(List<ListModel> res) {
}
