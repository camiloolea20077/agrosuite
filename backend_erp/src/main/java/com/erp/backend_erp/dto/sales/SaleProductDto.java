package com.erp.backend_erp.dto.sales;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SaleProductDto {
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal price;
}
