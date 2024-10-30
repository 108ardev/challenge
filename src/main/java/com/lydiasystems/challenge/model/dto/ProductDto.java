package com.lydiasystems.challenge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ProductDto {

    @NotEmpty(message = "Product name cannot be empty")
    @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
    private final String name;
    @NotEmpty(message = "Product description cannot be empty")
    @Size(min = 1, max = 512, message = "Product description must be between 1 and 512 characters")
    private final String description;
    @PositiveOrZero(message = "Product stock count must be positive or zero")
    private final Integer stockCount;
    @Positive(message = "Product price must be positive")
    private final BigDecimal price;
}
