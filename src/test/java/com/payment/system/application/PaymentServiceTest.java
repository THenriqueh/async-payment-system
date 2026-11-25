package com.payment.system.application;

import com.payment.system.domain.Payment;
import com.payment.system.domain.PaymentStatus;
import com.payment.system.domain.exception.PaymentNotFoundException;
import com.payment.system.domain.ports.in.ProcessPaymentCommand;
import com.payment.system.domain.ports.out.AuthorizationPort;
import com.payment.system.domain.ports.out.LoadPaymentPort;
import com.payment.system.domain.ports.out.PublishEventPort;
import com.payment.system.domain.ports.out.SavePaymentPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private SavePaymentPort savePaymentPort;
    @Mock private LoadPaymentPort loadPaymentPort;
    @Mock private PublishEventPort publishEventPort;
    @Mock private AuthorizationPort authorizationPort;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldReturnExistingPaymentWhenIdempotencyKeyAlreadyExists() {

        UUID key = UUID.randomUUID();
        Payment existing = new Payment(UUID.randomUUID(), new BigDecimal("100"), "joao", "maria", key);
        ProcessPaymentCommand command = new ProcessPaymentCommand(new BigDecimal("100"), "joao", "maria", key);

        when(loadPaymentPort.findByIdempotencyKey(key)).thenReturn(Optional.of(existing));
        Payment result = paymentService.requestPayment(command);


        assertEquals(existing, result);
        verify(savePaymentPort, never()).save(any());
        verify(publishEventPort, never()).publish(any());
    }

    @Test
    void shouldSaveAndPublishWhenPaymentIsNew() {
        UUID key = UUID.randomUUID();
        ProcessPaymentCommand command = new ProcessPaymentCommand(new BigDecimal("100"), "joao", "maria", key);

        when(loadPaymentPort.findByIdempotencyKey(key)).thenReturn(Optional.empty());
        when(savePaymentPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Payment result = paymentService.requestPayment(command);

        assertEquals(PaymentStatus.REQUESTED, result.getStatus());
        verify(savePaymentPort).save(any());
        verify(publishEventPort).publish(any());
    }
    @Test
    void shouldThrowWhenPaymentNotFound() {
        UUID id = UUID.randomUUID();

        when(loadPaymentPort.findById(id)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> paymentService.getRequestedPaymentById(id));
    }

    @Test
    void shouldSkipProcessingWhenPaymentIsNotRequested (){
        UUID id = UUID.randomUUID();
        Payment payment =  Payment.reconstitute(id, new BigDecimal("100"), "joao", "maria", PaymentStatus.CAPTURED, UUID.randomUUID());

        when(loadPaymentPort.findById(id)).thenReturn(Optional.of(payment));

        assertEquals(PaymentStatus.CAPTURED, paymentService.processPayment(id).getStatus());
        verify(authorizationPort, never()).authorize(any());
    }

    @Test
    void shouldCaptureWhenAuthorized() {
        UUID id = UUID.randomUUID();
        Payment payment = new Payment(id, new BigDecimal("100"), "joao", "maria", UUID.randomUUID());

        when(loadPaymentPort.findById(id)).thenReturn(Optional.of(payment));
        when(authorizationPort.authorize(any())).thenReturn(true);
        when(savePaymentPort.save(any())).thenReturn(payment);

        paymentService.processPayment(id);

        assertEquals(PaymentStatus.CAPTURED, payment.getStatus());
        verify(savePaymentPort).save(payment);
    }

    @Test
    void shouldFailWhenNotAuthorized() {
        UUID id = UUID.randomUUID();
        Payment payment = new Payment(id, new BigDecimal("100"), "joao", "maria", UUID.randomUUID());

        when(loadPaymentPort.findById(id)).thenReturn(Optional.of(payment));
        when(authorizationPort.authorize(any())).thenReturn(false);
        when(savePaymentPort.save(any())).thenReturn(payment);

        paymentService.processPayment(id);

        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        verify(savePaymentPort).save(payment);
    }


}