package com.payment.system.adapter.out.client;

import com.payment.system.adapter.out.client.dto.AuthorizationRequest;
import com.payment.system.adapter.out.client.dto.AuthorizationResponse;
import com.payment.system.domain.ports.out.AuthorizationPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
@Slf4j
@Component
@AllArgsConstructor
public class AuthorizationHttpAdapter implements AuthorizationPort {


    private final RestClient restClient;

    @Override
    public boolean authorize(BigDecimal valor) {
        log.info("Requesting authorization to client authorizer, whith value ={}", valor);
        AuthorizationResponse response = restClient.post()
                .uri("/authorize")
                .body(new AuthorizationRequest(valor))
                .retrieve()
                .body(AuthorizationResponse.class);

        return response.approved();
    }
}
