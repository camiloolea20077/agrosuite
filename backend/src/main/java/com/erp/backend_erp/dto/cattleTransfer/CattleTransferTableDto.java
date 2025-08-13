package com.erp.backend_erp.dto.cattleTransfer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CattleTransferTableDto {
        
    private Long id;

    private String transfer_type; // 'CATTLE' o 'BIRTH'

    private String origin_farm;   // Nombre de la finca origen

    private String destination_farm; // Nombre de la finca destino

    private String transfer_date;

    private String observations;

    private String created_by;  // Nombre del usuario que creó el traslado
}
