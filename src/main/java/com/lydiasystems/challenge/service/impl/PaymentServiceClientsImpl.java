package com.lydiasystems.challenge.service.impl;

import com.lydiasystems.challenge.service.PaymentServiceClients;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceClientsImpl implements PaymentServiceClients {

    private final PaymentServiceImpl paymentService;

    @Async(value = "asyncExecutor")
    public CompletableFuture<String> call(Long productId, int quantity) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                paymentService.pay(productId, quantity);
                return "success";
            } catch (Exception e) {
                log.error("Payment failed: ", e);
                return "error";
            }
        });
    }
}
