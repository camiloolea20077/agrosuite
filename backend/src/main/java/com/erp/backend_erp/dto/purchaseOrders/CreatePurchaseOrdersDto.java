package com.erp.backend_erp.dto.purchaseOrders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePurchaseOrdersDto {
    private String numeroOrden;
    private Long supplierId;
    private Long farmId;
    private Long insumoRequestId;
    private LocalDateTime fechaOrden;
    private LocalDateTime fechaEntregaEsperada;
    private LocalDateTime fechaEntregaReal;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal total;
    private String observaciones;
    private Long estado;
    private Long createdBy;
}
