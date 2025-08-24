package com.erp.backend_erp.dto.cattleDeath;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeathsTableDto {
    private Integer id;
    private String tipo_animal; // "GANADO" o "TERNERO" calculado
    private String numero_animal; // Número del ganado o ternero
    private String sexo_animal;
    private String fecha_muerte;
    private String motivo_muerte;
    private String causa_categoria;
    private String peso_muerte;
    private String nombre;
}
