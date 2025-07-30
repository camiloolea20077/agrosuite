package com.erp.backend_erp.dto.terceros;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoCompleteModelDto {
    private Long id;
    private String numeroIdentificacion;
    private String tipoIdentificacion;
    private String nombreRazonSocial;
    private String telefono;
    private String direccion;
}
