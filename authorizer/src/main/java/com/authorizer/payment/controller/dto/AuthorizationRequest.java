package com.authorizer.payment.controller.dto;

import java.math.BigDecimal;

public record AuthorizationRequest(BigDecimal valor) {


}
