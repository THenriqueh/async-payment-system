package com.authorizer.payment.controller.dto;

public record AuthorizationResponse(
        boolean approved,
        String reason) {
}
