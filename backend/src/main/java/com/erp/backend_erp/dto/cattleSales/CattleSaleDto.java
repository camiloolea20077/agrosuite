package com.erp.backend_erp.dto.cattleSales;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CattleSaleDto {

    private Long id;
    private String fechaVenta;
    private Double pesoTotal;
    private Double precioKilo;
    private Double precioTotal;
    private String destino;
    private Long farmId;
    private List<Long> cattleIds;
    private String comprador;
    
}
