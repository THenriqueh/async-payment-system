package com.payment.system.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.system.adapter.out.persistence.entity.OutboxEventEntity;
import com.payment.system.adapter.out.persistence.repository.OutboxRepository;
import com.payment.system.domain.events.PaymentRequestedEvent;
import com.payment.system.domain.ports.out.PublishEventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutboxPublishAdapter implements PublishEventPort {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(PaymentRequestedEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            OutboxEventEntity entity = new OutboxEventEntity(
                    UUID.randomUUID(),
                    event.id(),
                    "Payment",
                    "PaymentRequested",
                    json,
                    false,
                    Instant.now()
            );
            outboxRepository.save(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing event to outbox", e);
        }
    }
}