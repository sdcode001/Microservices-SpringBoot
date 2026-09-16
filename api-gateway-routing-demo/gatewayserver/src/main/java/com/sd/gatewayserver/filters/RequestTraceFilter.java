package com.sd.gatewayserver.filters;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.logging.Level;
import org.slf4j.Logger;

/**
 * This is a custom filter to trace the incoming requests throughout the downstream microservices
 * where it travels by adding correlation-id in the request header.
 * All the downstream microservices log the request correlation-id in every steps so that that in
 * case of any error we can trace the request path with the correlation-id.
 *
 * Custom filters should implement GlobalFilter interface and override the filter() method with the
 * business logic. ServerWebExchange object gives the access to request and response bounded to that
 * exchange and GatewayFilterChain object used to invoke the next filter in the filter chain
 * Here @Order is used to define the execution order of the filter in the filter chain
 * */

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    private FilterUtility filterUtility;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if(isCorrelationIdPresent(requestHeaders)){
           logger.info("eazybank-correlation-id found in RequestTraceFilter: {}", filterUtility.getCorrelationId(requestHeaders));
        }
        else{
            String correlationId = generateCorrelationId();
            exchange = filterUtility.setCorrelationId(exchange, correlationId);
            logger.info("eazybank-correlation-id is generated in RequestTraceFilter: {}", correlationId);
        }
        return chain.filter(exchange);
    }

    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders){
        return filterUtility.getCorrelationId(requestHeaders) != null;
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }
}
