package com.erp.backend_erp.dto.inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryDto {
    private Long id;
    private String nombre_insumo;
    private String unidad;
    private Integer cantidad_total;
    private String descripcion;
    private Long farmId;
}
