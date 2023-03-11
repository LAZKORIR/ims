package com.ims.apigateway.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Configuration
@EnableWebFluxSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConf {

    private final ConfigProperties configProperties;

    public SecurityConf(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http.oauth2ResourceServer()
                .jwt()
                .jwtDecoder(reactiveJwtDecoder());
        http.oauth2ResourceServer();

        http.authorizeExchange()
                .pathMatchers("/actuator/**", "/csrf", "/monitoring")
                .permitAll()
                .anyExchange().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new HttpStatusServerAccessDeniedHandler(HttpStatus.UNAUTHORIZED)) //  Dealing with unauthorized
                .and().csrf().disable();
        http.headers().contentSecurityPolicy("default-src 'none'");

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() throws Exception{
        String publicKey;
        Resource resource;
        if (configProperties.getActiveProfile().equals("production")) {
            resource = new ClassPathResource("ms-auth-prod.cert");
        } else {
            resource = new ClassPathResource("ms-auth-uat.cert");
        }

        try {
            publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new NimbusReactiveJwtDecoder(getPublicKeyFromString(publicKey));
    }

    public static RSAPublicKey getPublicKeyFromString(String key) throws GeneralSecurityException {
        byte[] encoded = Base64.getMimeDecoder().decode(key);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
    }

}