package com.redis_in_spring.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final Err err;

    public CustomException(Err err) {
        super(err.getMessage());
        this.err = err;
    }
}
