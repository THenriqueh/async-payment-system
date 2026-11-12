package com.payment.system.application;

import com.payment.system.domain.Payment;
import com.payment.system.domain.events.PaymentRequestedEvent;
import com.payment.system.domain.ports.in.ProcessPaymentCommand;
import com.payment.system.domain.ports.in.ProcessPaymentUseCase;
import com.payment.system.domain.ports.out.PublishEventPort;
import com.payment.system.domain.ports.out.SavePaymentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService implements ProcessPaymentUseCase {

    private final SavePaymentPort savePaymentPort;
    private final PublishEventPort publishEventPort;

    @Override
    public Payment processPayment(ProcessPaymentCommand processPaymentCommand) {

        Optional<Payment> existing = savePaymentPort.findByIdempotencyKey(processPaymentCommand.idempotencyKey());
        if (existing.isPresent()) {
            return existing.get();
        }
        Payment payment = new Payment(
                UUID.randomUUID(),
                processPaymentCommand.valor(),
                processPaymentCommand.sender(),
                processPaymentCommand.recipient(),
                processPaymentCommand.idempotencyKey());
        Payment savedPayment = savePaymentPort.save(payment);
        publishEventPort.publish(PaymentRequestedEvent.from(savedPayment));

        return savedPayment;
    }
}