package com.lydiasystems.challenge.service;

import com.lydiasystems.challenge.entity.DTO.BankPaymentRequest;
import com.lydiasystems.challenge.entity.DTO.BankPaymentResponse;
import com.lydiasystems.challenge.entity.Payment;
import com.lydiasystems.challenge.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@Transactional
public class PaymentService {

    private Logger logger = LoggerFactory.getLogger(Transactional.class);

    private BankService bankService;
    private PaymentRepository paymentRepository;

    public void Transactional(BankService bankService, PaymentRepository paymentRepository) {
        this.bankService = bankService;
        this.paymentRepository = paymentRepository;
    }

    public void pay(BigDecimal price) {
        //pay with bank
        BankPaymentRequest request = new BankPaymentRequest();
        request.setPrice(price);
        BankPaymentResponse response = bankService.pay(request);

        //insert records
        Payment payment = new Payment();
        payment.setBankResponse(response.getResultCode());
        payment.setPrice(price);
        paymentRepository.save(payment);
        logger.info("Payment saved successfully!");
    }
}
