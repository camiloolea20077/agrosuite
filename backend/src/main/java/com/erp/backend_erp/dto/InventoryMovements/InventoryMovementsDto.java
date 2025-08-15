package com.erp.backend_erp.dto.InventoryMovements;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryMovementsDto {
    private Long id;
    private Long inventoryId;
    private Long farmId;
    private Long tipoMovimientoId;
    private Long estadoId;
    private BigDecimal cantidad;
    private String unidadMedida;
    private BigDecimal cantidadAnterior;
    private BigDecimal cantidadNueva;
    private LocalDateTime fechaMovimiento;
    private LocalDateTime fechaProgramada;
    private Long employeeId;
    private String numeroDocumento;
    private String observaciones;
    private String notas;
    private BigDecimal cantidadDevuelta;
    private BigDecimal cantidadUsada;
    private LocalDateTime fechaDevolucion;
    private Boolean estaCerrado;
    private String ubicacionOrigen;
    private String ubicacionDestino;
    private Boolean requiereAprobacion;
    private Long aprobadoPor;
    private LocalDateTime fechaAprobacion;
    private Boolean esAutomatico;
    private Long movimientoPadreId;
    private Long createdBy;
    private Long updatedBy;
}
