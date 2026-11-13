package com.payment.system.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record PaymentRequest(
        @NotNull @NotBlank String sender,
        @NotNull @NotBlank String recipient,
        @NotNull  String idempotencyKey,
        @NotNull  String valor) {
}
