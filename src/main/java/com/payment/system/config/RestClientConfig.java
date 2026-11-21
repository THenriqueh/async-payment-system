package com.payment.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${authorizer.url}")
    private String authorizerUrl;

    @Bean
    public RestClient authorizationRestClient() {
        return RestClient.builder()
                .baseUrl(authorizerUrl)
                .build();
    }
}