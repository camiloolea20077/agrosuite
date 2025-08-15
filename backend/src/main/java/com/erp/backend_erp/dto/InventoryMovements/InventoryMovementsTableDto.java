package com.erp.backend_erp.dto.InventoryMovements;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryMovementsTableDto {
    private Long id;
    private LocalDateTime fecha;
    private String insumo;
    private String tipoMovimiento;
    private Long cantidad;
    private String documento;
    private String estado;
    private String observaciones;
    private Long inventoryId;
    private Long tipoMovimientoId;
    private Long estadoId;
}
