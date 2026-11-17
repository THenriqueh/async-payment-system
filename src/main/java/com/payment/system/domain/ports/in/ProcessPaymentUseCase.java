package com.payment.system.domain.ports.in;

import com.payment.system.domain.Payment;

import java.util.UUID;

public interface ProcessPaymentUseCase {

    Payment processPayment(UUID paymentId);
}
