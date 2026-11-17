package com.payment.system.domain.ports.out;

import com.payment.system.domain.Payment;

import java.util.Optional;
import java.util.UUID;

public interface LoadPaymentPort {

    Optional<Payment> findById(UUID paymentId);

    Optional<Payment> findByIdempotencyKey(UUID idempotencyKey);

}
