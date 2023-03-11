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

//    @Value("${g2.keyOwner}")
//    private String keyOwner;

//    @Value("${auth.basicAuthUsername}")
//    private String basicAuthUsername;
//
//    @Value("${auth.basicAuthPassword}")
//    private String basicAuthPassword;


}
