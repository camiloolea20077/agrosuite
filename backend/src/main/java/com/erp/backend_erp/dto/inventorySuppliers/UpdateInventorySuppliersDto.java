package com.erp.backend_erp.dto.inventorySuppliers;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInventorySuppliersDto {
    private Long id;
    private Long inventoryId;
    private Long supplierId;
    private String codigoProveedor;
    private BigDecimal precioReferencia;
    private Long esPrincipal;
    private Long activo;
}
