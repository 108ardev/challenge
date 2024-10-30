package com.lydiasystems.challenge.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceClientsImplTest {

    @Mock
    private PaymentServiceImpl paymentService;

    @InjectMocks
    private PaymentServiceClientsImpl paymentServiceClients;

    @Test
    public void testCall_Success() throws ExecutionException, InterruptedException {
        // given
        doNothing().when(paymentService).pay(anyLong(), anyInt());
        // when
        CompletableFuture<String> result = paymentServiceClients.call(1L, 2);
        // then
        assertThat(result.get()).isEqualTo("success");
        verify(paymentService, times(1)).pay(1L, 2);
    }

    @Test
    public void testCall_Error() throws ExecutionException, InterruptedException {
        // given
        doThrow(new RuntimeException("Payment failed")).when(paymentService).pay(anyLong(), anyInt());
        // when
        CompletableFuture<String> result = paymentServiceClients.call(1L, 2);
        // then
        assertThat(result.get()).isEqualTo("error");
        verify(paymentService, times(1)).pay(1L, 2);
    }
}
