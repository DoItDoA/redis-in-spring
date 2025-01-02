package com.redis_in_spring.domain.hash.request;

import com.redis_in_spring.common.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HashRequest(BaseRequest baseRequest,
                          @NotBlank @NotNull String field,
                          @NotBlank @NotNull String name) {
}
