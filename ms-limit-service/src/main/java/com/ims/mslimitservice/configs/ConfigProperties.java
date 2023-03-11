package com.ims.mslimitservice.configs;

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

    @Value("${limit.rabbitmq.exchange}")
    private String limitExchange;

    @Value("${limit.rabbitmq.routingkey}")
    private String limitRoutingKey;

    @Value("${limit.rabbitmq.queue}")
    private String limitQueue;

}
