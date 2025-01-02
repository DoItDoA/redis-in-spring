package com.redis_in_spring.domain.sortedSet.request;

import com.redis_in_spring.common.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SortedSetRequest(BaseRequest baseRequest,
                               @NotNull @NotBlank String name,
                               @NotNull Double score) {
}
