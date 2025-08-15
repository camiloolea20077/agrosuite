package com.erp.backend_erp.dto.suppliers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuppliersDto {
    private Long id;
    private String codigo;
    private String nombre;
    private String contacto;
    private String telefono;
    private String email;
    private String direccion;
    private Long activo;
}
