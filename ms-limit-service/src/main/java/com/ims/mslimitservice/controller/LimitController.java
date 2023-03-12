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

    @PostMapping("/check-limit")
    public Mono<ResponseEntity<ApiResponse>> checkLimit( @Valid @RequestBody ApiRequest apiRequest){

        return limitService.checkLimit(apiRequest);
    }

    @PostMapping("/addUser")
    public Mono<ResponseEntity<ApiResponse>> addUser( @Valid @RequestBody ApiRequest apiRequest){

        return limitService.addUser(apiRequest);
    }
    @PostMapping("/getUser")
    public Mono<ResponseEntity<ApiResponse>> getUser( @Valid @RequestBody ApiRequest apiRequest){

        return limitService.getUser(apiRequest);
    }
    @GetMapping("/getAllUsers")
    public Mono<ResponseEntity<ApiResponse>> getAllUsers(){

        return limitService.getAllUsers();
    }
}
