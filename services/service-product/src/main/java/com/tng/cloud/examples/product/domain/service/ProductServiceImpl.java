package com.tng.cloud.examples.product.domain.service;

import com.tng.cloud.examples.product.domain.model.Product;
import com.tng.cloud.examples.product.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public ProductDto create(String name, BigDecimal price) {
        log.info("Creating product {} with price={}", name, price);
        Product product = Product.create(name, price);
        return ProductDto.from(product);
    }

    @Override
    public List<ProductDto> all() {
        return List.of(
                create("product 1", BigDecimal.valueOf(15.23)),
                create("product 2", BigDecimal.valueOf(88.88)),
                create("product 3", BigDecimal.valueOf(982.32))
        );
    }
}
