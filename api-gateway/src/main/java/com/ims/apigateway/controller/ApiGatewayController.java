package com.ims.apigateway.controller;

import com.ims.apigateway.dto.ApiRequest;
import com.ims.apigateway.dto.ApiResponse;
import com.ims.apigateway.service.ApiGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ApiGatewayController {

    @Autowired
    ApiGatewayService apiGatewayService;

    @PostMapping("/addUser")
    public Mono<ResponseEntity<ApiResponse>> addUser(@Valid @RequestBody ApiRequest apiRequest){

        return apiGatewayService.addUser(apiRequest);

    }

    @PostMapping("/getUser")
    public Mono<ResponseEntity<ApiResponse>> getUser(@Valid @RequestBody ApiRequest apiRequest){

        return apiGatewayService.getUser(apiRequest);

    }

    @GetMapping("/getAllUsers")
    public Mono<ResponseEntity<ApiResponse>> getAllUsers(){

        return apiGatewayService.getAllUsers();

    }

    @PostMapping("/check-limit")
    public Mono<ResponseEntity<ApiResponse>> checkLimit(@Valid @RequestBody ApiRequest apiRequest){

        return apiGatewayService.checkLimit(apiRequest);
    }

    @PostMapping("/request-loan")
    public Mono<ResponseEntity<ApiResponse>> requestLoan(@Valid @RequestBody ApiRequest apiRequest){

        return apiGatewayService.requestLoan(apiRequest);
    }

    @PostMapping("/loan-repayment")
    public Mono<ResponseEntity<ApiResponse>> repayLoan(@Valid @RequestBody ApiRequest apiRequest){

        return apiGatewayService.repayLoan(apiRequest);
    }
}
