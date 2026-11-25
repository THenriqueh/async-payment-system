package com.authorizer.payment.controller;


import com.authorizer.payment.controller.dto.AuthorizationRequest;
import com.authorizer.payment.controller.dto.AuthorizationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/authorize")
@Slf4j
public class AuthorizerController {

    private static final BigDecimal LIMIT = new BigDecimal("5000");

    @PostMapping
    public AuthorizationResponse authorize(@RequestBody AuthorizationRequest request) {
        log.info("Received authorization request: {}", request);
        if (request.valor().compareTo(LIMIT) <= 0) {
            log.info("Authorization approved for value: {}", request.valor());
            return new AuthorizationResponse(true, "aprovado");
        } else {
            log.warn("Authorization rejected for value: {}", request.valor());
            return new AuthorizationResponse(false, "limite excedido");
        }
    }

}
