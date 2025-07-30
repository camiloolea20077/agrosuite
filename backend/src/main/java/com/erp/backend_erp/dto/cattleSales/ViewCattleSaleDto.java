package com.erp.backend_erp.dto.cattleSales;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewCattleSaleDto {
    private Long id;
    private String numeroFactura;
    private LocalDate fechaVenta;
    private String horaEmision;
    private String tipoVenta;
    private String moneda;
    private String formaPago;

    // Comprador
    private Long terceroId; // ID del tercero
    private String comprador;           // Nombre o razón social

    // Datos de la venta
    private String destino;
    private String observaciones;
    private BigDecimal precioKilo;
    private BigDecimal pesoTotal;

    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal descuentos;
    private BigDecimal total;

    private List<CreateCattleSaleItemDto> items;
}
