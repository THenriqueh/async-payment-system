package com.payment.system.domain.ports.in;

import com.payment.system.domain.Payment;

public interface ProcessPaymentUseCase {

    Payment processPayment(ProcessPaymentCommand processPaymentCommand);
}
