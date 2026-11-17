package com.payment.system.domain.ports.in;

import com.payment.system.domain.Payment;

public interface RequestPaymentUseCase {

    Payment requestPayment(ProcessPaymentCommand processPaymentCommand);

}
