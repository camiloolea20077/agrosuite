package com.erp.backend_erp.dto.sales;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleDto {
    private Long id;
    private Long customerId;
    private BigDecimal total;
    private String status;
    private LocalDateTime createdAt;
    private List<SaleProductDto> products;
}
