package com.payment.system.adapter.in.web.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull @NotBlank String sender,
        @NotNull @NotBlank String recipient,
        @NotNull  String idempotencyKey,
        @NotNull  String valor) {
}
