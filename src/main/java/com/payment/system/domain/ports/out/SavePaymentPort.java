package com.payment.system.domain.ports.out;

import com.payment.system.domain.Payment;

public interface SavePaymentPort {

    Payment save(Payment payment);




}
