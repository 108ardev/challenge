package com.lydiasystems.challenge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class BankPaymentRequest {

    @Positive(message = "Product price must be positive")
    private final BigDecimal price;
}
