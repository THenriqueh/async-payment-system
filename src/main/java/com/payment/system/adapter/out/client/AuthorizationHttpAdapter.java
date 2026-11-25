package com.payment.system.adapter.out.client;

import com.payment.system.adapter.out.client.dto.AuthorizationRequest;
import com.payment.system.adapter.out.client.dto.AuthorizationResponse;
import com.payment.system.adapter.out.client.exception.AuthorizationUnavailableException;
import com.payment.system.domain.ports.out.AuthorizationPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name = "authorizer", fallbackMethod = "authorizeFallback")
    public boolean authorize(BigDecimal valor) {
        log.info("Requesting authorization to client authorizer, whith value ={}", valor);
        AuthorizationResponse response = restClient.post()
                .uri("/authorize")
                .body(new AuthorizationRequest(valor))
                .retrieve()
                .body(AuthorizationResponse.class);

        if (response == null) {
            throw new AuthorizationUnavailableException(valor);
        }

        return response.approved();
    }
    /**
     * Fallback invoked by Resilience4j when the authorizer call fails or the circuit
     * is open; the {@link Throwable} parameter carries the cause.
     */
    public boolean authorizeFallback(BigDecimal valor, Throwable throwable) {
        log.error("Authorization service unavailable for value={}, reason={}", valor, throwable.getMessage());
        throw new AuthorizationUnavailableException(valor, throwable);
    }
}
