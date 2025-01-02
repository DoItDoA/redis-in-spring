package com.redis_in_spring.domain.list.request;

import com.redis_in_spring.common.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ListRequest(BaseRequest baseRequest,
                          @NotBlank @NotNull String name) {
}
