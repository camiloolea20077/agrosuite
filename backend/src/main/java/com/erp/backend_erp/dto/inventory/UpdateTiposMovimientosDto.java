package com.erp.backend_erp.dto.inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTiposMovimientosDto {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Long esEntrada;
    private Long esSalida;
    private Long requiereEmpleado;
    private Long requiereAprobacion;
    private Long activo;
}
