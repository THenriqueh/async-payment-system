package com.payment.system.adapter.out.messaging;

import com.payment.system.domain.events.PaymentRequestedEvent;
import com.payment.system.domain.ports.out.PublishEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventKafkaAdapter {

    private final KafkaTemplate<String, PaymentRequestedEvent> kafkaTemplate;


    public void publish(PaymentRequestedEvent event) {
        kafkaTemplate.send("payments.requested", event.id().toString(), event);
    }
}
