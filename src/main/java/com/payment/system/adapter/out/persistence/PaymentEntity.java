package com.payment.system.adapter.out.persistence;

import com.payment.system.domain.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "payments")
public class PaymentEntity {
    @Id
    private UUID id;
    private BigDecimal valor;
    private String sender;
    private String recipient;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Column(unique = true, nullable = false)
    private UUID idempotencyKey;

}
