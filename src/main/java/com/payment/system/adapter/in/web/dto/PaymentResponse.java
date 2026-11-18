package com.payment.system.adapter.in.web.dto;

import com.payment.system.domain.Payment;
import com.payment.system.domain.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        String sender,
        String recipient,
        PaymentStatus status,
        BigDecimal valor) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getSender(),
                payment.getRecipient(),
                payment.getStatus(),
                payment.getValor()
        );
    }
}
