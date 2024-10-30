package com.lydiasystems.challenge.service.impl;

import com.lydiasystems.challenge.service.PaymentServiceClients;
import com.lydiasystems.challenge.service.PaymentSimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PaymentSimulationServiceImpl implements PaymentSimulationService {

    private final PaymentServiceClients paymentServiceClients;

    /**
     * Simulation of 100 user payments.
     */
    public void simulate100Payments(Long productId, int quantity) {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            CompletableFuture<String> future = paymentServiceClients.call(productId, quantity);
            futures.add(future);
        }

        futures.forEach(CompletableFuture::join);
    }
}
