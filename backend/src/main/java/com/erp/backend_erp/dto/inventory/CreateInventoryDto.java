package com.erp.backend_erp.dto.inventory;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInventoryDto {
    private String nombre_insumo;
    private String unidad;
    private Integer cantidad_total;
    private String descripcion;
    private Long farmId;
}
