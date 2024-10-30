package com.lydiasystems.challenge.service;

import java.util.concurrent.CompletableFuture;

public interface PaymentServiceClients {

    CompletableFuture<String> call(Long productId, int quantity);
}
