package com.redis_in_spring.domain.strategy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValueWithTTL<T> {
    T value;
    Long ttl;
}
