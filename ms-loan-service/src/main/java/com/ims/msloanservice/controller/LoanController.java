package com.ims.msloanservice.controller;

import com.ims.msloanservice.dto.ApiRequest;
import com.ims.msloanservice.dto.ApiResponse;
import com.ims.msloanservice.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/loan")
public class LoanController {
    @Autowired
    LoanService loanService;

    @PostMapping("/request-loan")
    public Mono<ResponseEntity<ApiResponse>> requestLoan(@Valid @RequestBody ApiRequest apiRequest){

        return loanService.requestLoan(apiRequest);
    }
}
