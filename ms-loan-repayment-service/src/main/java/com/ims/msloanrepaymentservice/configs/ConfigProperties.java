package com.ims.msloanrepaymentservice.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties
@Component
@Data
public class ConfigProperties {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${lendiplatform.rabbitmq.exchange}")
    private String exchange;

    @Value("${lendiplatform.rabbitmq.routingkey}")
    private String routingKey;

    @Value("${loan.rabbitmq.queue}")
    private String loanQueue;

    @Value("${notification.rabbitmq.queue}")
    private String notificationQueue;


}
