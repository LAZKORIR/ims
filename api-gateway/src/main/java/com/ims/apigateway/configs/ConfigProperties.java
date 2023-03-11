package com.ims.apigateway.configs;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConfigProperties {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${lendiplatform.rabbitmq.exchange}")
    private String exchange;

    @Value("${lendiplatform.rabbitmq.routingkey}")
    private String routingKey;

    @Value("${loan.rabbitmq.queue}")
    private String loanQueue;

    @Value("${loan.repay.rabbitmq.queue}")
    private String loanRepayQueue;

    @Value("${limit.rabbitmq.queue}")
    private String limitQueue;

    @Value("${numberOfAllowedRequests}")
    private Integer numberOfAllowedRequests;

    @Value("${checkLimitEndpoint}")
    private String checkLimitEndpoint;

    @Value("${auth.username}")
    private String authUsername;

    @Value("${auth.password}")
    private String authPassword;


}
