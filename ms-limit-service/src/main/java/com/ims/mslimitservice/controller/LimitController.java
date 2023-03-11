package com.ims.mslimitservice.controller;

import com.ims.mslimitservice.model.ApiRequest;
import com.ims.mslimitservice.model.ApiResponse;
import com.ims.mslimitservice.service.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class LimitController {

    @Autowired
    LimitService limitService;

    @PostMapping("/checkLimit")
    public Mono<ResponseEntity<ApiResponse>> checkLimit( @Valid @RequestBody ApiRequest apiRequest){

        return limitService.checkLimit(apiRequest);
    }
}
