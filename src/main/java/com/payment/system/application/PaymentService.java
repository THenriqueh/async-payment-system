package com.payment.system.application;

import com.payment.system.domain.Payment;
import com.payment.system.domain.events.PaymentRequestedEvent;
import com.payment.system.domain.ports.in.ProcessPaymentCommand;
import com.payment.system.domain.ports.in.ProcessPaymentUseCase;
import com.payment.system.domain.ports.out.PublishEventPort;
import com.payment.system.domain.ports.out.SavePaymentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService implements ProcessPaymentUseCase {

    private final SavePaymentPort savePaymentPort;
    private final PublishEventPort publishEventPort;

    @Override
    public Payment processPayment(ProcessPaymentCommand processPaymentCommand) {
        log.info("Processing payment for sender={}, recipient={}", processPaymentCommand.sender(), processPaymentCommand.recipient());

        Optional<Payment> existing = savePaymentPort.findByIdempotencyKey(processPaymentCommand.idempotencyKey());
        if (existing.isPresent()) {
            log.warn("Payment with idempotencyKey={} already exists, returning existing payment with id={}", processPaymentCommand.idempotencyKey(), existing.get().getId());
            return existing.get();
        }
        Payment payment = new Payment(
                UUID.randomUUID(),
                processPaymentCommand.valor(),
                processPaymentCommand.sender(),
                processPaymentCommand.recipient(),
                processPaymentCommand.idempotencyKey());
        Payment savedPayment = savePaymentPort.save(payment);
        log.info("Payment created with id={}, status={}", payment.getId(), payment.getStatus());

        publishEventPort.publish(PaymentRequestedEvent.from(savedPayment));
        log.info("PaymentRequestedEvent published for payment id={}", savedPayment.getId());
        return savedPayment;
    }
}