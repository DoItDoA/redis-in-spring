package com.redis_in_spring.domain.hash.controller;

import com.redis_in_spring.domain.hash.request.HashRequest;
import com.redis_in_spring.domain.hash.response.HashResponse;
import com.redis_in_spring.service.HashService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hash")
@RequiredArgsConstructor
public class HashController {
    private final HashService hashService;

    @PostMapping
    public void putHash(@RequestBody @Valid HashRequest req) {
        hashService.putInHash(req.baseRequest().key(), req.field(), req.name());
    }

    @GetMapping
    public HashResponse getHash(@RequestParam @Valid String key,
                                  @RequestParam @Valid String field) {
        return hashService.getFromHash(key, field);
    }

}
