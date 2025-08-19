package com.erp.backend_erp.dto.births;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MigrarTerneroDto {
    @NotNull(message = "El ID del nacimiento es requerido")
    private Long birthId;
    
    @NotNull(message = "La decisión es requerida")
    @Pattern(regexp = "cria|venta", message = "La decisión debe ser 'cria' o 'venta'")
    private String decision;
}
