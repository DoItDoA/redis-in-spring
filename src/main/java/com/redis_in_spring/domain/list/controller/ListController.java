package com.redis_in_spring.domain.list.controller;

import com.redis_in_spring.domain.list.request.ListRequest;
import com.redis_in_spring.domain.list.response.ListResponse;
import com.redis_in_spring.service.ListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/list")
public class ListController {
    private final ListService listService;

    @PostMapping("/left")
    public void setValueToList(@RequestBody @Valid ListRequest req) {
        listService.addToListLeft(req.baseRequest().key(), req.name());
    }

    @PostMapping("/right")
    public void setValueToRight(@RequestBody @Valid ListRequest req) {
        listService.addToListRight(req.baseRequest().key(), req.name());
    }

    @GetMapping
    public ListResponse getAll(@RequestParam String key) {
        return listService.getAllData(key);
    }
}
