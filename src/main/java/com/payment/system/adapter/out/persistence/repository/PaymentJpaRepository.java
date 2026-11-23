package com.payment.system.adapter.out.persistence.repository;

import com.payment.system.adapter.out.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {

    Optional<PaymentEntity> findByIdempotencyKey(UUID idempotencyKey);


}
