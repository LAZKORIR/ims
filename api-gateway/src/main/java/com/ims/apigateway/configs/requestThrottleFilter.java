package com.ims.apigateway.configs;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
//@Component
class requestThrottleFilter implements WebFilter {

    @Autowired
    ConfigProperties configProperties;

    //private int MAX_REQUESTS_PER_SECOND = configProperties.getNumberOfRequests(); //or whatever you want it to be
    private LoadingCache<String, Integer> requestCountsPerIpAddress;
    public requestThrottleFilter(){
        super();
        requestCountsPerIpAddress = Caffeine.newBuilder().
                expireAfterWrite(1, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    private boolean isMaximumRequestsPerSecondExceeded(String clientIpAddress){
        Integer requests = 0;
        requests = requestCountsPerIpAddress.get(clientIpAddress);
        if(requests != null){
            if(requests > configProperties.getNumberOfAllowedRequests()) {
                requestCountsPerIpAddress.asMap().remove(clientIpAddress);
                requestCountsPerIpAddress.put(clientIpAddress, requests);
                return true;
            }
        } else {
            requests = 0;
        }
        requests++;
        requestCountsPerIpAddress.put(clientIpAddress, requests);
        return false;
    }
    public String getClientIP(ServerHttpRequest request) {
//        String xfHeader = request.getHeaders("X-Forwarded-For");
        System.out.println(request.getHeaders().get("Authorization").get(0));
        String xfHeader = request.getHeaders().get("Authorization").get(0);
        if (xfHeader == null){
//            return request.getRemoteAddr();
            return request.getLocalAddress().toString();
        }
        return xfHeader.split(",")[0];
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain filterChain) {
        ServerHttpRequest httpServletRequest = (ServerHttpRequest) exchange.getRequest();
        ServerHttpResponse httpServletResponse = (ServerHttpResponse) exchange.getResponse();
        String clientIpAddress = getClientIP((ServerHttpRequest) httpServletRequest);
        System.out.println("****************");
        //System.out.println(clientIpAddress);
        System.out.println("****************");
        if(isMaximumRequestsPerSecondExceeded(clientIpAddress)){
//            httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpServletResponse.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
//            httpServletResponse.getWriter().write("Too many requests");
            return Mono.empty();
        }
//        filterChain.doFilter(servletRequest, servletResponse);
        return filterChain.filter(exchange);
    }
}