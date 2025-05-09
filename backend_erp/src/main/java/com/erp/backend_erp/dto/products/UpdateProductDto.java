package com.erp.backend_erp.dto.products;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductDto {
    private Long id;
    private String name;
    private String sku;
    private BigDecimal price;
    private BigDecimal quantity;
}
