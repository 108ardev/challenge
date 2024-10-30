package com.lydiasystems.challenge.service.impl;

import com.lydiasystems.challenge.mapper.ProductMapper;
import com.lydiasystems.challenge.model.dto.ProductDto;
import com.lydiasystems.challenge.model.entity.Product;
import com.lydiasystems.challenge.repository.ProductRepository;
import com.lydiasystems.challenge.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProduct(Long productId) {
        Product product = findProductById(productId);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDto(products);
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long productId, ProductDto dto) {
        Product product = findProductById(productId);
        productMapper.update(dto, product);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new EntityNotFoundException(
                    String.format("Product with ID: %d not found", productId));
        }
        productRepository.deleteById(productId);
    }

    @Override
    @Transactional
    public void reduceStock(Long productId, int quantity) {
        Product product = findProductById(productId);
        if (product.getStockCount() < quantity) {
            throw new IllegalArgumentException(
                    String.format("Not enough stock for product %s. Available: %d, Requested: %d",
                            product.getName(), product.getStockCount(), quantity));
        }
        product.setStockCount(product.getStockCount() - quantity);
        productRepository.save(product);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Product with ID: %d not found", productId)));
    }
}
