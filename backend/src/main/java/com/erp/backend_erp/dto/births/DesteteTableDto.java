package com.erp.backend_erp.dto.births;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesteteTableDto {

    private Long id;
    private String numero_cria;
    private LocalDate fecha_nacimiento;
    private LocalDate fecha_proxima_destete;
    private Long dias_restantes;
}
