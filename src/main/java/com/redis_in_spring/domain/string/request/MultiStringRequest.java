package com.redis_in_spring.domain.string.request;

import com.redis_in_spring.common.request.BaseRequest;
import jakarta.validation.constraints.NotNull;

public record MultiStringRequest(BaseRequest baseRequest,
                                 @NotNull String[] names) {
}
