package com.erp.backend_erp.dto.inventory;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateStockDto {
    private Long inventoryId;
    private BigDecimal newQuantity;
}