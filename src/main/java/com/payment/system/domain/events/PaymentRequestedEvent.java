package com.payment.system.domain.events;

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

}
