package com.redis_in_spring.domain.sortedSet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SortedSetModel {
    String name;
    Double score;
}
