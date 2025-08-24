package com.erp.backend_erp.dto.cattleDeath;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CattleDeathDto {
    private Integer id;
    private Integer cattleId;
    private Integer birthId;
    private String fechaMuerte;
    private String motivoMuerte;
    private String descripcionDetallada;
    private String pesoMuerte;
    private String causaCategoria;
    private Long usuarioRegistro;
    private Long farmId;
}
