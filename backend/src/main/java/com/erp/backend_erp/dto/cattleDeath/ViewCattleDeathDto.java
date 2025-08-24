package com.erp.backend_erp.dto.cattleDeath;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewCattleDeathDto {
    private Integer id;
    private Integer cattleId;
    private Integer birthId;
    private String tipoAnimal;
    private String numeroAnimal;
    private String sexoAnimal;
    private String colorAnimal;
    private String tipoAnimalDetalle;
    private String fechaMuerte;
    private String motivoMuerte;
    private String descripcionDetallada;
    private String pesoMuerte;
    private String causaCategoria;
    private String usuarioRegistroNombre;
}
