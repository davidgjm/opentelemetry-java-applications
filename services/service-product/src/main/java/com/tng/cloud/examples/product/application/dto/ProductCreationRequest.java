package com.tng.cloud.examples.product.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tng.cloud.examples.product.dto.ProductDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
public final class ProductCreationRequest {
    @NotBlank
    @JsonProperty(required = true)
    private String name;

    @PositiveOrZero
    @JsonProperty(required = true)
    private BigDecimal price;


    public ProductDto productDto() {
        return ProductDto.builder()
                .name(name)
                .price(price)
                .build();
    }
}
