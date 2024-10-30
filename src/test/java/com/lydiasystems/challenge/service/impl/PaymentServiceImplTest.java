package com.lydiasystems.challenge.service.impl;

import com.lydiasystems.challenge.mapper.ProductMapper;
import com.lydiasystems.challenge.model.dto.BankPaymentRequest;
import com.lydiasystems.challenge.model.dto.BankPaymentResponse;
import com.lydiasystems.challenge.model.dto.ProductDto;
import com.lydiasystems.challenge.model.entity.Payment;
import com.lydiasystems.challenge.model.entity.Product;
import com.lydiasystems.challenge.repository.PaymentRepository;
import com.lydiasystems.challenge.validator.PaymentServiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private BankServiceImpl bankService;

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentServiceValidator paymentServiceValidator;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private final Long productId = 1L;
    private final int quantity = 2;
    private ProductDto productDto;
    private Product product;

    @BeforeEach
    void setUp() {
        productDto = createProductDto();
        product = createProduct();
    }

    @Test
    public void testPay_Success() {
        // given
        when(productService.getProduct(productId)).thenReturn(productDto);
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(bankService.pay(any(BankPaymentRequest.class)))
                .thenReturn(new BankPaymentResponse("200"));
        // when
        paymentService.pay(productId, quantity);
        // then
        verify(productService, times(1)).reduceStock(productId, quantity);
        verify(paymentServiceValidator, times(1)).validatePaymentResponse(any(BankPaymentResponse.class));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    public void testPay_InsufficientStock() {
        // given
        when(productService.getProduct(productId)).thenReturn(productDto);
        when(productMapper.toEntity(productDto)).thenReturn(product);
        doThrow(new IllegalArgumentException("Not enough stock")).when(productService).reduceStock(productId, quantity);
        // when & then
        assertThatThrownBy(() -> paymentService.pay(productId, quantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not enough stock");
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void testPay_BankPaymentFails() {
        // given
        when(productService.getProduct(productId)).thenReturn(productDto);
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(bankService.pay(any(BankPaymentRequest.class)))
                .thenReturn(new BankPaymentResponse("500"));
        doThrow(new RuntimeException("Payment failed at bank")).when(paymentServiceValidator).validatePaymentResponse(any(BankPaymentResponse.class));
        // when & then
        assertThatThrownBy(() -> paymentService.pay(productId, quantity))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Payment failed at bank");
        verify(paymentServiceValidator, times(1)).validatePaymentResponse(any(BankPaymentResponse.class));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void testPay_ValidationFailure() {
        // given
        when(productService.getProduct(productId)).thenReturn(productDto);
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(bankService.pay(any(BankPaymentRequest.class)))
                .thenReturn(new BankPaymentResponse("200"));
        doThrow(new RuntimeException("Validation failed")).when(paymentServiceValidator).validatePaymentResponse(any(BankPaymentResponse.class));
        // when & then
        assertThatThrownBy(() -> paymentService.pay(productId, quantity))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Validation failed");
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    private ProductDto createProductDto() {
        return new ProductDto("Product", "Description", 10, new BigDecimal("100.00"));
    }

    private Product createProduct() {
        return new Product(productId, "Product", "Description", 10, new BigDecimal("100.00"), 1L);
    }
}
