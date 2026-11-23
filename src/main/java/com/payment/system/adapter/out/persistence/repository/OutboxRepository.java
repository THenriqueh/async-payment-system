package com.payment.system.adapter.out.persistence.repository;

import com.payment.system.adapter.out.persistence.entity.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEventEntity, UUID> {

    List<OutboxEventEntity> findByPublishedFalse();
}
