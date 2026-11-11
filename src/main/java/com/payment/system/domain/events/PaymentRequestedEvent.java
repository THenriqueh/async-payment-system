package com.payment.system.domain.events;

import com.payment.system.domain.Payment;
import com.payment.system.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentRequestedEvent(
        UUID id,
        BigDecimal valor,
        String sender,
        String recipient,
        UUID idempotencyKey,
        Instant occurredAt
) {
    public static PaymentRequestedEvent from(Payment payment) {
        return new PaymentRequestedEvent(
                payment.getId(),
                payment.getValor(),
                payment.getSender(),
                payment.getRecipient(),
                payment.getIdempotencyKey(),
                Instant.now()
        );
    }
}
