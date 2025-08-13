package com.erp.backend_erp.dto.cattleTransfer;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewCattleTransferDto {
    private Long id;
    private String transferType;
    private Long originFarmId;
    private Long destinationFarmId;
    private String transferDate;
    private String observations;
    
    private List<CattleTransferItemDto> items;
}
