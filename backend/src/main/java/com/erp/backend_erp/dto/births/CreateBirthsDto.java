package com.erp.backend_erp.dto.births;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBirthsDto {
    private Long tipo_vaca;
    private Long nombre_toro;
    private String fecha_nacimiento;
    private String numero_cria;
    private String sexo;
    private String color_cria;
    private String observaciones;
}
