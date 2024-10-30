package com.lydiasystems.challenge.validator;

import com.lydiasystems.challenge.model.dto.BankPaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentServiceValidatorTest {

    private PaymentServiceValidator paymentServiceValidator;

    private BankPaymentResponse response;

    @BeforeEach
    public void setUp() {
        paymentServiceValidator = new PaymentServiceValidator();
    }

    @Test
    public void testValidatePaymentResponse_SuccessfulResponse() {
        // given
        response = BankPaymentResponse.builder()
                .resultCode("200")
                .build();
        // when & then
        assertThatCode(() -> paymentServiceValidator.validatePaymentResponse(response))
                .doesNotThrowAnyException();
    }

    @Test
    public void testValidatePaymentResponse_NullResponse() {
        assertThatThrownBy(() -> paymentServiceValidator.validatePaymentResponse(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Payment failed at bank");
    }

    @Test
    public void testValidatePaymentResponse_UnsuccessfulResponse() {
        // given
        response = BankPaymentResponse.builder()
                .resultCode("500")
                .build();
        // when & then
        assertThatThrownBy(() -> paymentServiceValidator.validatePaymentResponse(response))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Payment failed at bank");
    }
}
