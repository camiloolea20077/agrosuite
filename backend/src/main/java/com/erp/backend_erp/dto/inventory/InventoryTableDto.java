package com.erp.backend_erp.dto.inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryTableDto {
    private Long id;
    private String nombre_insumo;
    private String unidad;
    private String descripcion;
    private Long stock_actual;
    private String estado_stock;
}
