package com.erp.backend_erp.dto.births;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesteteDto {

    private Long id;
    
    @JsonProperty("numero_cria")
    private String numeroCria;
    
    @JsonProperty("sexo")
    private String sexo;
    
    @JsonProperty("fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @JsonProperty("edad_meses")
    private Integer edadMeses;
    
    @JsonProperty("peso_cria")
    private Double pesoCria;
    
    @JsonProperty("color_cria")
    private String colorCria;
    
    @JsonProperty("nombre_madre")
    private String nombreMadre;
    
    @JsonProperty("nombre_padre")
    private String nombrePadre;
    
    @JsonProperty("farm_id")
    private Long farmId;
}
