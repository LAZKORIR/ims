package com.ims.msnotificationservice.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;

@ConfigurationProperties
@Component
@Data
public class ConfigProperties {

    @Value("${email.sender}")
    private String emailSender;

    @Value("${lendiplatform.rabbitmq.exchange}")
    private String exchange;

    @Value("${lendiplatform.rabbitmq.routingkey}")
    private String routingKey;

    @Value("${notification.rabbitmq.queue}")
    private String notificationQueue;



}
