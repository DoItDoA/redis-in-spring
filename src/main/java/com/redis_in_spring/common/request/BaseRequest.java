package com.redis_in_spring.common.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record BaseRequest(@NotBlank @NotNull String key) {
}
