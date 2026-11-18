package com.payment.system.domain.ports.in;

import com.payment.system.domain.Payment;

import java.util.UUID;

public interface RequestPaymentUseCase {

    Payment getRequestedPaymentById(UUID id);

    Payment requestPayment(ProcessPaymentCommand processPaymentCommand);

}
