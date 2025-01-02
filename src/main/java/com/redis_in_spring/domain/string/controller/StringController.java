package com.redis_in_spring.domain.string.controller;

import com.redis_in_spring.domain.string.request.MultiStringRequest;
import com.redis_in_spring.domain.string.request.StringRequest;
import com.redis_in_spring.domain.string.response.StringResponse;
import com.redis_in_spring.service.StringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/string")
public class StringController {
    private final StringService stringService;

    @PostMapping
    public void setString(@RequestBody @Valid StringRequest req) {
        stringService.set(req);
    }

    @GetMapping
    public StringResponse getString(@RequestParam @Valid String key) {
        return stringService.get(key);
    }

    @PostMapping("/multi")
    public void multiSetString(@RequestBody @Valid MultiStringRequest req) {
        stringService.multiSet(req);
    }

    @GetMapping("/multi")
    public StringResponse multiGetString(@RequestParam @Valid String key) {
        return stringService.multiGet(key);
    }
}
