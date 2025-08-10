package com.erp.backend_erp.dto.cattleTransfer;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CattleTransferDto {
    
    private Long id;
    private String transferType; // "CATTLE" o "BIRTH"
    private Long originFarmId;
    private Long destinationFarmId;
    private String transferDate;
    private String observations;
    private Long createdBy;
    private LocalDateTime createdAt;

    private List<CattleTransferItemDto> items; // lista de ítems
}
