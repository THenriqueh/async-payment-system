package com.payment.system.adapter.in.web;

import com.payment.system.domain.Payment;
import com.payment.system.domain.ports.in.ProcessPaymentCommand;
import com.payment.system.domain.ports.in.RequestPaymentUseCase;
import com.payment.system.adapter.in.web.dto.PaymentRequest;
import com.payment.system.adapter.in.web.dto.PaymentResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final RequestPaymentUseCase processPaymentUseCase;

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable  UUID paymentId) {
       Payment payment = processPaymentUseCase.getRequestedPaymentById(paymentId);
        return ResponseEntity.ok(PaymentResponse.from(payment));
    }
    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        ProcessPaymentCommand command = new ProcessPaymentCommand(
                new BigDecimal(paymentRequest.valor()),
                paymentRequest.sender(),
                paymentRequest.recipient(),
                UUID.fromString(paymentRequest.idempotencyKey())
        );
        Payment payment = processPaymentUseCase.requestPayment(command);

        return ResponseEntity.accepted().body(PaymentResponse.from(payment));
    }
}
