package com.payment.system.domain.ports.out;

import com.payment.system.domain.events.PaymentRequestedEvent;

public interface PublishEventPort {

    void publish(PaymentRequestedEvent event);

}
