package com.erp.backend_erp.dto.purchaseOrders;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePurchaseOrderDetailsDto {
    private Long id;
    private Long purchaseOrderId;
    private Long inventoryId;
    private BigDecimal cantidadOrdenada;
    private BigDecimal cantidadRecibida;
    private String unidadMedida;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private String observaciones;
}
