package com.payment.system.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.system.adapter.out.messaging.PaymentEventKafkaAdapter;
import com.payment.system.adapter.out.persistence.entity.OutboxEventEntity;
import com.payment.system.adapter.out.persistence.repository.OutboxRepository;
import com.payment.system.domain.events.PaymentRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * Scheduled relay for the outbox pattern: polls unpublished events and pushes
 * them to Kafka, decoupling the database write from the broker.
 * Requires {@code @EnableScheduling} on the application class to run.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final PaymentEventKafkaAdapter paymentEventKafkaAdapter;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 1000)
    public void publishPendingEvents() {
        List<OutboxEventEntity> pending = outboxRepository.findByPublishedFalse();
        for (OutboxEventEntity entity : pending) {
            try {
                PaymentRequestedEvent event = objectMapper.readValue(
                        entity.getJsonPayload(), PaymentRequestedEvent.class);
                paymentEventKafkaAdapter.publish(event);
                entity.setPublished(true);
                outboxRepository.save(entity);
                log.info("Published event with id={}", entity.getId());
            } catch (JsonProcessingException e) {
                log.error("Failed to deserialize event with id={}, error={}", entity.getId(), e.getMessage());
            }
        }
    }
}
