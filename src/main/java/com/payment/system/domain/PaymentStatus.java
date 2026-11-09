package com.payment.system.domain;

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
            return false; // No transitions allowed from CAPTURED
        }
    },
    FAILED{
        @Override
        public boolean canTransitionTo(PaymentStatus newStatus) {
            return false; // No transitions allowed from FAILED
        }
    };


    public abstract boolean canTransitionTo(PaymentStatus newStatus);
}
