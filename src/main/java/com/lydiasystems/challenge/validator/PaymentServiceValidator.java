package com.lydiasystems.challenge.validator;

import com.lydiasystems.challenge.model.dto.BankPaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceValidator {

    public void validatePaymentResponse(BankPaymentResponse response) {
        if (response == null || !"200".equals(response.getResultCode())) {
            throw new RuntimeException("Payment failed at bank");
        }
    }
}
