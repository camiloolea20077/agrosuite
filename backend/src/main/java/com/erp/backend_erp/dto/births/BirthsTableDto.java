package com.erp.backend_erp.dto.births;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BirthsTableDto {
    private Long id;
    private String numero_ganado;
    private String numero_toro;
    private String fecha_nacimiento;
    private String numero_cria;
    private String sexo;
    private String color_cria;
    private String observaciones;
}
