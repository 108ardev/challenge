package com.lydiasystems.challenge.service;

import com.lydiasystems.challenge.model.dto.BankPaymentRequest;
import com.lydiasystems.challenge.model.dto.BankPaymentResponse;
import com.lydiasystems.challenge.model.entity.Payment;
import com.lydiasystems.challenge.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BankService bankService;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void pay(BigDecimal price) {
        //pay with bank
        BankPaymentRequest request = BankPaymentRequest.builder()
                .price(price)
                .build();
        BankPaymentResponse response = bankService.pay(request);

        //insert records
        Payment payment = Payment.builder()
                .bankResponse(response.getResultCode())
                .price(price).build();
        paymentRepository.save(payment);
        log.info("Payment saved successfully!");
    }
}
