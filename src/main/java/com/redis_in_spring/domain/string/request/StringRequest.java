package com.redis_in_spring.domain.string.request;

import com.redis_in_spring.common.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StringRequest(BaseRequest baseRequest,
                            @NotNull @NotBlank String name) {

}
