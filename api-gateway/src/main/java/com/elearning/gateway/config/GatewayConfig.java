package com.elearning.gateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Bean;

@Configuration
public class GatewayConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

}
