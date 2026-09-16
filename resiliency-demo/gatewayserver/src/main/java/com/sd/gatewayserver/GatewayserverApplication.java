package com.sd.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

    @Bean
    public RouteLocator buildRoutingConfig(RouteLocatorBuilder routeLocatorBuilder){
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/eazybank/accounts/**")
                        .filters(f -> f
                                .rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config
                                        .setName("accountsCircuitBreaker")
                                        .setFallbackUri("forward:/contact-support")
                                ) //Adding circuit-breaker for accounts-ms traffic.
                        )
                        .uri("lb://ACCOUNTS")

                )
                .route(p -> p
                        .path("/eazybank/loans/**")
                        .filters(f -> f
                                .rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .retry(config -> config
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(500), Duration.ofMillis(5000),2, true)
                                ) //Adding retry for loans-ms traffic.
                        )
                        .uri("lb://LOANS")

                )
                .route(p -> p
                        .path("/eazybank/cards/**")
                        .filters(f -> f
                                .rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .requestRateLimiter(config -> config
                                        .setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(userKeyResolver())
                                ) //Adding rate-limiter for cards-ms traffic.
                        )
                        .uri("lb://CARDS")

                )
                .build();
    }

    /**
     * RedisRateLimiter of spring-cloud-gateway is based on work done at Stripe.
     * It's uses the Token-Bucket algorithm.
     * Here are 3 config property based on those it works:
     *  defaultReplenishRate - Number of requests to allow per second. i.e- Rate at which token bucket is filled.
     *  defaultBurstCapacity - The maximum number of requests a user is allowed in a second. i.e- The number of token bucket can hold.
     *  defaultRequestedTokens - The number of tokens a request costs.
     *
     * When a user surpasses the permitted number of requests within the designated timeframe, additional requests are
     * declined with HTTP 429 - Too Many Requests status.
     * */
    @Bean
    public RedisRateLimiter redisRateLimiter(){
        return new RedisRateLimiter(
                1,
                1,
                1
        );
    }


    /**
     * KeyResolver tells rate-limiter the criteria based on which rate limiting will work.
     * It can be - user base, session based, ip based, user-quota based etc
     *
     * Here it's request header's user basis. So per user basis rete-limiting will work.
     * */
    @Bean
    KeyResolver userKeyResolver(){
        return exchange -> Mono
                .justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
                .defaultIfEmpty("anonymous");
    }
}
