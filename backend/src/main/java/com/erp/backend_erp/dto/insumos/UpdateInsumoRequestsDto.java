package com.erp.backend_erp.dto.insumos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInsumoRequestsDto {
    private Long id;
    private String numeroSolicitud;
    private Long farmId;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaNecesaria;
    private Long prioridad;
    private String justificacion;
    private String observaciones;
    private Long estado;
    private Long procesadoPor;
    private LocalDateTime fechaProcesamiento;
}
