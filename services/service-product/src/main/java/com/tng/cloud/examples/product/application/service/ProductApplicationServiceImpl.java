package com.tng.cloud.examples.product.application.service;

import com.tng.cloud.examples.product.application.dto.ProductCreationRequest;
import com.tng.cloud.examples.product.domain.service.ProductService;
import com.tng.cloud.examples.product.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Valid
@Slf4j
@Service
public class ProductApplicationServiceImpl implements ProductApplicationService {
    private final ProductService productService;

    public ProductApplicationServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ProductDto createProduct(ProductCreationRequest request) {
        Assert.notNull(request,"request data cannot be null!");
        return productService.create(request.getName(), request.getPrice());
    }

    @Override
    public List<ProductDto> findAll() {
        return productService.all();
    }
}
