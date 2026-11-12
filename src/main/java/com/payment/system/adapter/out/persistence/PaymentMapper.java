package com.payment.system.adapter.out.persistence;

import com.payment.system.domain.Payment;
import org.springframework.stereotype.Component;


@Component
public class PaymentMapper {

    public PaymentEntity toEntity(Payment payment) {
        return new PaymentEntity(
                payment.getId(),
                payment.getValor(),
                payment.getSender(),
                payment.getRecipient(),
                payment.getStatus(),
                payment.getIdempotencyKey()
        );

    }

    public Payment toDomain(PaymentEntity entity) {
       return Payment.reconstitute(
                entity.getId(),
                entity.getValor(),
                entity.getSender(),
                entity.getRecipient(),
                entity.getStatus(),
                entity.getIdempotencyKey()
       );

    }
}


