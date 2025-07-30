package com.erp.backend_erp.dto.terceros;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTerceroDto {
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private String nombreRazonSocial;
    private String direccion;
    private String telefono;
    private String correo;
    private Long farmId;
}
