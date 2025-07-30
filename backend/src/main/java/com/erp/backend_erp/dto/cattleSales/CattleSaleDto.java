package com.erp.backend_erp.dto.cattleSales;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CattleSaleDto {
    
    private Long id;
    private String tipoVenta;
    private String fechaVenta;
    private String horaEmision;
    private String numeroFactura;
    private Double precioKilo;
    private Double pesoTotal;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal descuentos;
    private BigDecimal total;
    private String moneda;
    private String formaPago;
    private String destino;
    private String observaciones;
    private Long farmId;
    private Long terceroId;
    private List<Long> cattleIds;
    private List<CreateCattleSaleItemDto> items;
    
}
