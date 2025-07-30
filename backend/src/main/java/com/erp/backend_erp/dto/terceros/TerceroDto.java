package com.erp.backend_erp.dto.terceros;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TerceroDto {
    private Long id;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private String nombreRazonSocial;
    private String direccion;
    private String telefono;
    private Long farmId;
    private String correo;
}
