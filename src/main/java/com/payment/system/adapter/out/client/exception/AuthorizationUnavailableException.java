package com.payment.system.adapter.out.client.exception;

import java.math.BigDecimal;

public class AuthorizationUnavailableException extends RuntimeException {

    public AuthorizationUnavailableException(BigDecimal valor, Throwable cause) {
        super("Authorization unavailable for value: " + valor, cause);
    }

    // novo — pro caso em que não há exceção causadora
    public AuthorizationUnavailableException(BigDecimal valor) {
        super("Authorization unavailable for value: " + valor);
    }
}
