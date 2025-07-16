package com.erp.backend_erp.dto.products;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private Long companyId;
    private String name;
    private String sku;
    private BigDecimal price;
    private BigDecimal quantity;
    private String code;
    private String description;
    private LocalDateTime entry_date;
    private Long supplier_id;
}
