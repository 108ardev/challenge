package com.lydiasystems.challenge.service.impl;

import com.lydiasystems.challenge.mapper.ProductMapper;
import com.lydiasystems.challenge.model.dto.ProductDto;
import com.lydiasystems.challenge.model.entity.Product;
import com.lydiasystems.challenge.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product", "Test Description", 10, new BigDecimal("100.00"), 1L);
        productDto = new ProductDto("Test Product", "Test Description", 10, new BigDecimal("100.00"));
    }

    @Test
    public void testGetProduct_Success() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);
        // when
        ProductDto result = productService.getProduct(1L);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository, times(1)).findById(1L);
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    public void testGetProduct_NotFound() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> productService.getProduct(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product with ID: 1 not found");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateProduct_Success() {
        // given
        when(productMapper.toEntity(any(ProductDto.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);
        // when
        ProductDto result = productService.createProduct(productDto);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productMapper, times(1)).toEntity(productDto);
        verify(productRepository, times(1)).save(product);
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    public void testUpdateProduct_Success() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        doNothing().when(productMapper).update(any(ProductDto.class), any(Product.class));
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);
        // when
        ProductDto result = productService.updateProduct(1L, productDto);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository, times(1)).findById(1L);
        verify(productMapper, times(1)).update(productDto, product);
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    public void testDeleteProduct_Success() {
        // given
        when(productRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(productRepository).deleteById(anyLong());
        // when
        productService.deleteProduct(1L);
        // then
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        // given
        when(productRepository.existsById(anyLong())).thenReturn(false);
        // when & then
        assertThatThrownBy(() -> productService.deleteProduct(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product with ID: 1 not found");

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testReduceStock_Success() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        // when
        productService.reduceStock(1L, 5);
        // then
        assertThat(product.getStockCount()).isEqualTo(5);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testReduceStock_NotEnoughStock() {
        // given
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        // when & then
        assertThatThrownBy(() -> productService.reduceStock(1L, 15))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not enough stock for product");
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    public void testGetProducts_Success() {
        // given
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        when(productMapper.toDto(anyList())).thenReturn(Collections.singletonList(productDto));
        // when
        List<ProductDto> result = productService.getProducts();
        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).isEqualTo("Test Product");
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(1)).toDto(anyList());
    }
}
