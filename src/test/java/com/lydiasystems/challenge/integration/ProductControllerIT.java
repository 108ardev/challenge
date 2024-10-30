package com.lydiasystems.challenge.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lydiasystems.challenge.model.dto.ProductDto;
import com.lydiasystems.challenge.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductServiceImpl productService;

    @Test
    public void testCreateProduct() throws Exception {
        ProductDto productDto = new ProductDto("New Product", "A test product", 10, new BigDecimal("100.00"));

        when(productService.createProduct(productDto)).thenReturn(productDto);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    public void testGetProducts() throws Exception {
        ProductDto product1 = new ProductDto("Product A", "Description A", 10, new BigDecimal("100.00"));
        ProductDto product2 = new ProductDto("Product B", "Description B", 20, new BigDecimal("200.00"));

        when(productService.getProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[1].name").value("Product B"));
    }

    @Test
    public void testGetProductById() throws Exception {
        ProductDto product = new ProductDto("Product A", "Description A", 10, new BigDecimal("100.00"));

        when(productService.getProduct(1L)).thenReturn(product);

        mockMvc.perform(get("/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product A"));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductDto updateDto = new ProductDto("Updated Product", "Updated description", 5, new BigDecimal("150.00"));

        when(productService.updateProduct(1L, updateDto)).thenReturn(updateDto);

        mockMvc.perform(put("/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/v1/products/1"))
                .andExpect(status().isNoContent());
    }
}
