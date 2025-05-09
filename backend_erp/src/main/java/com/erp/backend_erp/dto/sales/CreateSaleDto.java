package com.erp.backend_erp.dto.sales;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSaleDto {
    private Long customerId;
    private List<SaleProductDto> products;
}