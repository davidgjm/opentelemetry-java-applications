package com.tng.cloud.examples.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tng.cloud.examples.product.domain.model.Product;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Data
public class ProductDto {
    @JsonProperty(required = true)
    private final String name;
    private BigDecimal price;

    public static ProductDto from(@NotNull Product product) {
        Assert.notNull(product,"product cannot be null!");
        return ProductDto.builder()
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
