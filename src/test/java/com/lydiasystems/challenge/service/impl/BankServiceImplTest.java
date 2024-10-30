package com.lydiasystems.challenge.service.impl;

import com.lydiasystems.challenge.model.dto.BankPaymentRequest;
import com.lydiasystems.challenge.model.dto.BankPaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankServiceImplTest {

    private BankServiceImpl bankService;

    private BankPaymentRequest request;
    private BankPaymentResponse response;

    @BeforeEach
    public void setUp() {
        bankService = new BankServiceImpl();
    }

    @Test
    public void testSuccessfulPayment() {
        // given
        request = BankPaymentRequest.builder()
                .price(new java.math.BigDecimal("100.00"))
                .build();
        // when
        response = bankService.pay(request);
        // then
        assertThat(response).isNotNull();
        assertThat(response.getResultCode()).isEqualTo("200");
    }

    @Test
    public void testNullResponseOnInterruptedPayment() {
        // given
        request = BankPaymentRequest.builder()
                .price(new java.math.BigDecimal("100.00"))
                .build();
        // when & then
        assertThatThrownBy(() -> {
            Thread.currentThread().interrupt();
            bankService.pay(request);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("interrupted");
    }

    @Test
    public void testRetryMechanismWithMultipleAttempts() {
        // given
        request = BankPaymentRequest.builder()
                .price(new java.math.BigDecimal("100.00"))
                .build();

        long startTime = System.currentTimeMillis();
        // when
        response = bankService.pay(request);
        long duration = System.currentTimeMillis() - startTime;
        // then
        assertThat(response).isNotNull();
        assertThat(response.getResultCode()).isEqualTo("200");
        assertThat(duration).isGreaterThanOrEqualTo(5000);
    }
}
