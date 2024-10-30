package com.lydiasystems.challenge.service.impl;

import com.lydiasystems.challenge.model.dto.BankPaymentRequest;
import com.lydiasystems.challenge.model.dto.BankPaymentResponse;
import com.lydiasystems.challenge.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Slf4j
@Service
public class BankServiceImpl implements BankService {

    /**
     * Bank Latency Simulation (avg: 5 seconds)
     */
    @Retryable(
            retryFor = InterruptedException.class,
            maxAttemptsExpression = "${config.retry-config.max-attempts}",
            backoff = @Backoff(delayExpression = "${config.retry-config.delay}")
    )
    public BankPaymentResponse pay(@Valid BankPaymentRequest request) {
        try {
            Thread.sleep(5000);
            return new BankPaymentResponse("200");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread was interrupted during payment processing", e);
            throw new RuntimeException("Payment process interrupted", e);
        }
    }
}
