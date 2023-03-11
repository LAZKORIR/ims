package com.ims.apigateway.utils;

import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

public class ResponseUtils {
    public static void writeErrorInfo(){

    }

    public static Mono<? extends Void> writeErrorInfo(ServerHttpResponse response, String s) {
        return null;
    }
}
