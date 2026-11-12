package com.payment.system.domain.ports.in;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessPaymentCommand(
        BigDecimal valor,
        String sender,
        String recipient,
        UUID idempotencyKey
) {
}
