package com.redis_in_spring.domain.string.response;

import com.redis_in_spring.domain.string.model.StringModel;

import java.util.List;

public record StringResponse(List<StringModel> response) {
}
