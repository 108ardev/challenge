package com.lydiasystems.challenge.service.impl;

import com.lydiasystems.challenge.mapper.ProductMapper;
import com.lydiasystems.challenge.model.dto.BankPaymentRequest;
import com.lydiasystems.challenge.model.dto.BankPaymentResponse;
import com.lydiasystems.challenge.model.dto.ProductDto;
import com.lydiasystems.challenge.model.entity.Payment;
import com.lydiasystems.challenge.model.entity.Product;
import com.lydiasystems.challenge.repository.PaymentRepository;
import com.lydiasystems.challenge.service.BankService;
import com.lydiasystems.challenge.service.PaymentService;
import com.lydiasystems.challenge.service.ProductService;
import com.lydiasystems.challenge.validator.PaymentServiceValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BankService bankService;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final PaymentRepository paymentRepository;
    private final PaymentServiceValidator paymentServiceValidator;

    @Transactional
    @Retryable(
            retryFor = RuntimeException.class,
            maxAttemptsExpression = "${config.retry-config.max-attempts}",
            backoff = @Backoff(delayExpression = "${config.retry-config.delay}")
    )
    public void pay(Long productId, int quantity) {
        ProductDto dto = productService.getProduct(productId);
        Product product = productMapper.toEntity(dto);
        productService.reduceStock(productId, quantity);

        BankPaymentRequest request = BankPaymentRequest.builder()
                .price(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .build();
        BankPaymentResponse response = bankService.pay(request);

        paymentServiceValidator.validatePaymentResponse(response);

        Payment payment = Payment.builder()
                .bankResponse(response.getResultCode())
                .price(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .build();
        paymentRepository.save(payment);
        log.info("Payment for product {} (quantity: {}) saved successfully!", product.getName(), quantity);
    }
}
