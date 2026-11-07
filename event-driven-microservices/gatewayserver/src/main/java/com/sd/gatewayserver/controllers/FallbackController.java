package com.sd.gatewayserver.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/contact-support")
    public Mono<String> contactSupport(){
        //Business logic to handle fallback (Send alert mail, Connect to backup server, etc)
        return Mono.just("Error occurred. Please try after some times or contact support.");
    }

}
