package com.payment.system.controller;

import com.payment.system.domain.Payment;
import com.payment.system.domain.ports.in.ProcessPaymentCommand;
import com.payment.system.domain.ports.in.ProcessPaymentUseCase;
import com.payment.system.dto.PaymentRequest;
import com.payment.system.dto.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        ProcessPaymentCommand command = new ProcessPaymentCommand(
                new BigDecimal(paymentRequest.valor()),
                paymentRequest.sender(),
                paymentRequest.recipient(),
                UUID.fromString(paymentRequest.idempotencyKey())
        );
        Payment payment = processPaymentUseCase.processPayment(command);

        return ResponseEntity.accepted().body(PaymentResponse.from(payment));
    }
}
