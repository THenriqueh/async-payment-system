package com.payment.system.adapter.in.messaging;

import com.payment.system.domain.Payment;
import com.payment.system.domain.events.PaymentRequestedEvent;
import com.payment.system.domain.ports.in.ProcessPaymentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventKafkaConsumer{

    private final ProcessPaymentUseCase processPaymentUseCase;

    @KafkaListener(topics = "payments.requested", groupId = "payment-processor")
    public void consume(PaymentRequestedEvent event) {
        log.info("Consuming event for payment id={}", event.id());
        processPaymentUseCase.processPayment(event.id());

    }

}


