package com.lydiasystems.challenge.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentSimulationServiceImplTest {

    @Mock
    private PaymentServiceClientsImpl paymentServiceClients;

    @InjectMocks
    private PaymentSimulationServiceImpl paymentSimulationService;

    @Test
    public void testSimulate100Payments_Success() {
        // given
        when(paymentServiceClients.call(anyLong(), anyInt()))
                .thenReturn(CompletableFuture.completedFuture("success"));
        // when
        paymentSimulationService.simulate100Payments(1L, 2);
        // then
        verify(paymentServiceClients, times(100)).call(anyLong(), anyInt());
    }

    @Test
    public void testSimulate100Payments_ErrorHandling() {
        // given
        when(paymentServiceClients.call(anyLong(), anyInt()))
                .thenReturn(CompletableFuture.completedFuture("error"));
        // when
        paymentSimulationService.simulate100Payments(1L, 2);
        // then
        verify(paymentServiceClients, times(100)).call(anyLong(), anyInt());
    }

    @Test
    public void testSimulate100Payments_MixedResults() {
        // given
        when(paymentServiceClients.call(anyLong(), anyInt()))
                .thenReturn(CompletableFuture.completedFuture("success"))
                .thenReturn(CompletableFuture.completedFuture("error"));
        // when
        paymentSimulationService.simulate100Payments(1L, 2);
        // then
        verify(paymentServiceClients, times(100)).call(anyLong(), anyInt());
    }
}
