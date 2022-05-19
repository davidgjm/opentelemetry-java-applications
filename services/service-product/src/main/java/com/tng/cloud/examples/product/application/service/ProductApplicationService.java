package com.tng.cloud.examples.product.application.service;

import com.tng.cloud.examples.product.application.dto.ProductCreationRequest;
import com.tng.cloud.examples.product.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductApplicationService {
    ProductDto createProduct(ProductCreationRequest request);

    List<ProductDto> findAll();
}
