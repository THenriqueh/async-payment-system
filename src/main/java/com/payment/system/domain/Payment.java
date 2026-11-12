package com.payment.system.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Payment {
    private final UUID id;
    private final BigDecimal valor;
    private final String sender;
    private final String recipient;
    private PaymentStatus status;
    private final UUID idempotencyKey;

    public Payment(UUID id, BigDecimal valor, String sender, String recipient, UUID idempotencyKey) {
        if(id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if(valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor must be greater than zero");
        }
        if (sender == null || sender.isBlank()) {
            throw new IllegalArgumentException("Sender cannot be null or empty");
        }
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("Recipient cannot be null or empty");
        }
        if (idempotencyKey == null) {
            throw new IllegalArgumentException("Idempotency Key cannot be null");
        }

        this.id = id;
        this.valor = valor;
        this.sender = sender;
        this.recipient = recipient;
        this.status = PaymentStatus.REQUESTED; // Default status is REQUESTED
        this.idempotencyKey = idempotencyKey;
    }

    public void changeStatus(PaymentStatus newStatus) {
        if (this.status.canTransitionTo(newStatus)) {
            this.status = newStatus;
        } else {
            throw new IllegalArgumentException("Invalid Status Transition");
        }
    }

    /**
     * Reconstructs a Payment from already-persisted data, preserving its original status.
     * For persistence-layer use only — do not use to create new payments
     * (for creation, use the constructor, which forces the REQUESTED status).
     */
    public static Payment reconstitute(UUID id, BigDecimal valor, String sender,
                                       String recipient, PaymentStatus status, UUID idempotencyKey) {
        Payment payment = new Payment(id, valor, sender, recipient, idempotencyKey);
        payment.status = status; // Set the status directly since it's a reconstitution
        return payment;
    }

}
