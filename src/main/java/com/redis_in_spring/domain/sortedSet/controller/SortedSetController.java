package com.redis_in_spring.domain.sortedSet.controller;

import com.redis_in_spring.domain.sortedSet.request.SortedSetRequest;
import com.redis_in_spring.domain.sortedSet.response.SortedSetResponse;
import com.redis_in_spring.service.SortedSetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sorted-set")
public class SortedSetController {
    private final SortedSetService sortedSetService;

    @PostMapping
    public void setSortedSet(@RequestBody @Valid SortedSetRequest req) {
        sortedSetService.setSortedSet(req);
    }

    @GetMapping
    public SortedSetResponse getSortedSet(@RequestParam @Valid String key,
                                          @RequestParam @Valid Double min,
                                          @RequestParam @Valid Double max) {
        return sortedSetService.getDataByRange(key, min, max);
    }

    @GetMapping("/top")
    public SortedSetResponse getTopN(@RequestParam @Valid String key,
                                     @RequestParam @Valid Integer n) {
        return sortedSetService.getTopN(key, n);
    }
}
