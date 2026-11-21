package com.payment.system.adapter.out.client.dto;

import java.math.BigDecimal;

public record AuthorizationResponse (
        boolean approved,
        String reason){
}
