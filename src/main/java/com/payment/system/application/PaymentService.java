package com.payment.system.application;

import com.payment.system.domain.Payment;
import com.payment.system.domain.PaymentStatus;
import com.payment.system.domain.events.PaymentRequestedEvent;
import com.payment.system.domain.exception.PaymentNotFoundException;
import com.payment.system.domain.ports.in.ProcessPaymentCommand;
import com.payment.system.domain.ports.in.ProcessPaymentUseCase;
import com.payment.system.domain.ports.in.RequestPaymentUseCase;
import com.payment.system.domain.ports.out.LoadPaymentPort;
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
public class PaymentService implements RequestPaymentUseCase, ProcessPaymentUseCase {

    private final SavePaymentPort savePaymentPort;
    private final LoadPaymentPort loadPaymentPort;
    private final PublishEventPort publishEventPort;


    @Override
    public Payment getRequestedPaymentById(UUID id) {
        return loadPaymentPort.findById(id).orElseThrow(() -> new PaymentNotFoundException(id));
    }

    @Override
    public Payment requestPayment(ProcessPaymentCommand processPaymentCommand) {
        log.info("Processing payment for sender={}, recipient={}", processPaymentCommand.sender(), processPaymentCommand.recipient());

        Optional<Payment> existing = loadPaymentPort.findByIdempotencyKey(processPaymentCommand.idempotencyKey());
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


    @Override
    public Payment processPayment(UUID paymentId) {

        Payment payment = loadPaymentPort.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
        log.info("Processing payment with id={}", payment.getId());

        if (payment.getStatus() != PaymentStatus.REQUESTED) {
            log.warn("Payment id={} already processed, status={}, skipping", payment.getId(), payment.getStatus());
            return payment;
        }
        payment.authorize();

        if (payment.getStatus() == PaymentStatus.AUTHORIZED) {
            log.info("Payment with id={} authorized successfully, status={}", payment.getId(), payment.getStatus());
            payment.capture();
            log.info("Payment with id={} captured, status={}", payment.getId(), payment.getStatus());
        } else {
            log.warn("Payment with id={} was not authorized, status={}", payment.getId(), payment.getStatus());
        }

        Payment savedPayment = savePaymentPort.save(payment);
        log.info("Payment with id={} processed successfully, status={}", savedPayment.getId(), savedPayment.getStatus());

        return savedPayment;
    }


}