package com.ims.msloanservice.config;

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

//    @Value("${auth.basicAuthUsername}")
//    private String basicAuthUsername;
//
//    @Value("${auth.basicAuthPassword}")
//    private String basicAuthPassword;


}
