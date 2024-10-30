package com.lydiasystems.challenge.service;

import com.lydiasystems.challenge.model.dto.BankPaymentRequest;
import com.lydiasystems.challenge.model.dto.BankPaymentResponse;

public interface BankService {

    BankPaymentResponse pay(BankPaymentRequest request);
}
