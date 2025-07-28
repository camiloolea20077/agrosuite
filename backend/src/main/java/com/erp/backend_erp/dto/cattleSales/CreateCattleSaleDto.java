package com.erp.backend_erp.dto.cattleSales;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCattleSaleDto {
    private String fechaVenta;
    private Double pesoTotal;
    private String tipoVenta;
    private Double precioKilo;
    private String destino;
    private Long farmId;
    private List<Long> cattleIds;
    private Double precioTotal; 
    private String comprador;
    private String observaciones;
    private List<CreateCattleSaleItemDto> items; 
}
