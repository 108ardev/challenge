package com.lydiasystems.challenge.entity.DTO;

import lombok.Data;

@Data
public class BankPaymentResponse {
    private String resultCode;
    public BankPaymentResponse(String resultCode) {
        this.resultCode = resultCode;
    }
}
