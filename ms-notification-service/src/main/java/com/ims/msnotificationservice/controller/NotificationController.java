package com.ims.msnotificationservice.controller;

import com.ims.msnotificationservice.model.ApiRequest;
import com.ims.msnotificationservice.model.ApiResponse;
import com.ims.msnotificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @PostMapping("/sendSms")
    public void sendSms(@Valid @RequestBody ApiRequest apiRequest){

    }

    @PostMapping("/sendEmail")
    public Mono<ResponseEntity<ApiResponse>> sendEmail(@Valid @RequestBody ApiRequest apiRequest){

        return notificationService.sendEmail(apiRequest);
    }
}
