package com.payment.system.domain;

/**
 * Payment lifecycle as a state machine; each constant declares its valid
 * transitions via {@link #canTransitionTo(PaymentStatus)}.
 */
public enum PaymentStatus {

    REQUESTED{
        @Override
        public boolean canTransitionTo(PaymentStatus newStatus) {
            return newStatus == AUTHORIZED || newStatus == FAILED;
        }
    },

    AUTHORIZED {
        @Override
        public boolean canTransitionTo(PaymentStatus newStatus) {
            return newStatus == CAPTURED || newStatus == FAILED;
        }
    },
    CAPTURED{
        @Override
        public boolean canTransitionTo(PaymentStatus newStatus) {
            return false;
        }
    },
    FAILED{
        @Override
        public boolean canTransitionTo(PaymentStatus newStatus) {
            return false;
        }
    };


    public abstract boolean canTransitionTo(PaymentStatus newStatus);
}
