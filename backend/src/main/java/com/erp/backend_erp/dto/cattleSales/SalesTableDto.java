package com.erp.backend_erp.dto.cattleSales;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesTableDto {
    private Long id;
    private String tipo_venta;
    private String fecha_venta;
    private String comprador;
    private String observaciones;
    private Long total_animales;
    private Long peso_total;
    private Long precio_promedio;
    private Long total_venta;
}
