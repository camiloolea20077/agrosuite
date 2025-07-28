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
    private String comprador;
    private String destino;
    private LocalDate fechaVenta;
    private String observaciones;
    private BigDecimal precioKilo;
    private BigDecimal pesoTotal;
    private BigDecimal precioTotal;
    private List<CreateCattleSaleItemDto> items;
}
