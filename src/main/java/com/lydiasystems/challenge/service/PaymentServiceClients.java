package com.lydiasystems.challenge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PaymentServiceClients {

    private final PaymentService paymentService;

    @Async
    public CompletableFuture<String> call(BigDecimal price) {
        paymentService.pay(price);
        return CompletableFuture.completedFuture("success");
    }
}
