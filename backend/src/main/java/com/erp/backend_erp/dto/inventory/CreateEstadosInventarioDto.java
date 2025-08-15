package com.erp.backend_erp.dto.inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEstadosInventarioDto {
    private String codigo;
    private String nombre;
    private String descripcion;
    private Long activo;
}
