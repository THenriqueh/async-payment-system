package com.payment.system.domain;


import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    @Test
    void shouldThrowWhenValueIsNegative(){
        UUID id = UUID.randomUUID();
        UUID key = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> new Payment(id, new BigDecimal(-10), "joao", "maria", key));
    }

    @Test
    void shouldCreatePaymentWithRequestedStatus() {
        Payment payment = new Payment(UUID.randomUUID(), new BigDecimal("100"),
                "joao", "maria", UUID.randomUUID());

        assertEquals(PaymentStatus.REQUESTED, payment.getStatus());
    }

    @Test
    void shouldThrowWhenCapturingFromRequested (){
        Payment payment = new Payment(UUID.randomUUID(), new BigDecimal("100"), "joao", "maria", UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, payment::capture);
    }

}
