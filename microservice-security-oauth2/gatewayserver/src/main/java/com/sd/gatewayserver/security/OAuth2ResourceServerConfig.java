package com.sd.gatewayserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;


/**
 * This Java class configures this application acting as an OAuth2 Resource Server. It defines how
 * incoming HTTP requests are authenticated with Auth Server(Configure in application.yaml) using
 * JWT access_token
 * */
@Configuration
@EnableWebFluxSecurity
public class OAuth2ResourceServerConfig {

    /**
     * This bean creates a security filter chain that authenticate incoming HTTP requests with OAuth2
     * Auth Server according to the rules configured.
     * Here it allows all GET requests without access_token verification but POST, PUT, DELETE requests
     * to /accounts, /loans, or /cards will be authenticated and authorization(role check) with their JWT access_token.
     * */
    @Bean
    public SecurityWebFilterChain indetityCheckFilterChain(ServerHttpSecurity serverHttpSecurity){
        serverHttpSecurity.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.GET).permitAll()
                .pathMatchers("/eazybank/accounts/**").hasRole("ACCOUNTS")
                .pathMatchers("/eazybank/loans/**").hasRole("LOANS")
                .pathMatchers("/eazybank/cards/**").hasRole("CARDS")

        )
        /**
         * Configures the resource server to expect a JWT in the Authorization: Bearer <TOKEN> header
         * of protected requests. It uses jwtAuthenticationConverter to validate the signature, expiration and role
         * of incoming request tokens (configured via properties spring.security.oauth2.resourceserver.jwt.jwk-set-uri
         * in application.yaml).
         * */
        .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                oAuth2ResourceServerSpec.jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
        );
        serverHttpSecurity.csrf(csrfSpec -> csrfSpec.disable());
        return serverHttpSecurity.build();
    }

    /**
     * This method sets out KeyCloakRoleConverter for token role validation
     * */
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor(){
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}
