package com.erp.backend_erp.dto.cattleTransfer;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CattleTransferTableDto {
        
    private Long id;

    private String transferType; // 'CATTLE' o 'BIRTH'

    private String originFarm;   // Nombre de la finca origen

    private String destinationFarm; // Nombre de la finca destino

    private LocalDateTime transferDate;

    private String observations;

    private String createdBy;  // Nombre del usuario que creó el traslado
}
