package com.lydiasystems.challenge.service;

import com.lydiasystems.challenge.model.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto getProduct(Long productId);

    List<ProductDto> getProducts();

    ProductDto createProduct(ProductDto dto);

    ProductDto updateProduct(Long productId, ProductDto dto);

    void deleteProduct(Long productId);

    void reduceStock(Long productId, int quantity);
}
