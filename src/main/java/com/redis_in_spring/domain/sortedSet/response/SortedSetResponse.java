package com.redis_in_spring.domain.sortedSet.response;


import com.redis_in_spring.domain.sortedSet.model.SortedSetModel;

import java.util.List;

public record SortedSetResponse(List<SortedSetModel> res) {
}
