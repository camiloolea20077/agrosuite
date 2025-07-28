package com.erp.backend_erp.dto.births;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BirthsDto {
    private Long id;
    private Long tipo_vaca;
    private Long nombre_toro;
    private String peso_cria;
    private String fecha_nacimiento;
    private String numero_cria;
    private String sexo;
    private String color_cria;
    private String observaciones;
    private Long farmId;
}
