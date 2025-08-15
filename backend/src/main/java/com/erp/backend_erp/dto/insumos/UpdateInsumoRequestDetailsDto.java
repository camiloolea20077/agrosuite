package com.erp.backend_erp.dto.insumos;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInsumoRequestDetailsDto {
    private Long id;
    private Long insumoRequestId;
    private Long inventoryId;
    private BigDecimal cantidadSolicitada;
    private BigDecimal cantidadAprobada;
    private String unidadMedida;
    private String justificacion;
    private String observaciones;
}
