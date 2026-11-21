package com.payment.system.domain.ports.out;

import java.math.BigDecimal;

public interface AuthorizationPort {
    boolean authorize(BigDecimal valor);
}
