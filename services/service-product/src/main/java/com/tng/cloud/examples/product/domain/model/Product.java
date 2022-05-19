package com.tng.cloud.examples.product.domain.model;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Valid
@Data
public final class Product {
    @NotBlank
    private final String name;
    private String description;

    @PositiveOrZero
    private BigDecimal price;

    public static Product create(String name, BigDecimal price) {
        Product product = new Product(name);
        product.setPrice(price);
        return product;
    }
}
