package com.payment.system.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "outbox_events")
public class OutboxEventEntity {

    @Id
    private UUID id;
    private UUID aggregateId;
    private String aggregateType;
    private String eventType;
    @Column(columnDefinition = "TEXT")
    private String jsonPayload;
    private boolean published;
    private Instant createdAt;


}
