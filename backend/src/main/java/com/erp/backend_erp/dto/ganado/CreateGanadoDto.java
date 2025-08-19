package com.erp.backend_erp.dto.ganado;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGanadoDto {
    private String tipo_ganado;
    private String numero_ganado;
    private String sexo;
    private String color;
    private String peso;
    private String fecha_nacimiento;
    private String lote_ganado;
    private String observaciones;
    private Long farmId;
    private Long activo;
    private String tipo_animal;
    private Integer embarazada;
    private String fecha_embarazo;
}
