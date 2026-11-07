package com.sd.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

/**
 * This is a custom filter to trace the outgoing requests to the client by adding request's correlation-id
 * in the response header. All the downstream microservices log the request correlation-id in every steps
 * so that in case of any error we can trace the request, response path with the correlation-id.
 *
 * Custom filters should implement GlobalFilter interface.
 * business logic. ServerWebExchange object(exchange) gives the access to request and response bounded to that
 * exchange and GatewayFilterChain object(chain) used to invoke the next filter in the filter chain
 * Here filter(exchange).then() executes after exchange's response comes. That's how its behave post filter.
 * */

@Configuration
public class ResponseTraceFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                String correlationId = filterUtility.getCorrelationId(requestHeaders);
                logger.info("Updated the correlation id to the outbound headers: {}", correlationId);
                exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_ID, correlationId);
            }));
        };
    }
}
