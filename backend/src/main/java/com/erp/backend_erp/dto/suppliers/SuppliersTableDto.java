package com.erp.backend_erp.dto.suppliers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuppliersTableDto {
    private Long id;
    private String nit;
    private String nombre;
    private String contacto;
    private String telefono;
    private String email;
    private Long activo;
}
