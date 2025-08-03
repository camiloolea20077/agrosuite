package com.erp.backend_erp.dto.cattleSales;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCattleSaleItemDto {
    private String numero_ganado;
    private String tipoOrigen;
    private Long idOrigen;
    private BigDecimal pesoVenta;
    private BigDecimal precioKilo;
    private BigDecimal precioTotal;
}
