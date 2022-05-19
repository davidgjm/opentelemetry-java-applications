package com.tng.cloud.examples.product.domain.service;

import com.tng.cloud.examples.product.domain.model.Product;
import com.tng.cloud.examples.product.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductDto create(String name, BigDecimal price);

    List<ProductDto> all();
}
