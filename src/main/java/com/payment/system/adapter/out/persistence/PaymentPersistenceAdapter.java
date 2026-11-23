package com.payment.system.adapter.out.persistence;

import com.payment.system.adapter.out.persistence.entity.PaymentEntity;
import com.payment.system.adapter.out.persistence.mapper.PaymentMapper;
import com.payment.system.adapter.out.persistence.repository.PaymentJpaRepository;
import com.payment.system.domain.Payment;
import com.payment.system.domain.ports.out.LoadPaymentPort;
import com.payment.system.domain.ports.out.SavePaymentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements SavePaymentPort, LoadPaymentPort {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public Payment save(Payment payment) {

        PaymentEntity entity = paymentMapper.toEntity(payment);
        PaymentEntity saved = paymentJpaRepository.save(entity);
        return paymentMapper.toDomain(saved);
    }

    @Override
    public Optional<Payment> findById(UUID paymentId) {

        return paymentJpaRepository.findById(paymentId).map(paymentMapper::toDomain);
    }

    @Override
    public Optional<Payment> findByIdempotencyKey(UUID idempotencyKey) {

        return paymentJpaRepository.findByIdempotencyKey(idempotencyKey).map(paymentMapper::toDomain);

    }

}

