package com.erp.backend_erp.dto.ganado;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GanadoTableDto {
    private Long id;
    private String tipo_ganado;
    private String numero_ganado;
    private String peso;
    private String tipo_animal;
    private Integer embarazo;
    private String fecha_nacimiento;
    private String lote_ganado;
    private String fecha_embarazo;
    private Long activo;
}
