package com.erp.backend_erp.dto.cattleTransfer;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCattleTransferDto {
    private String transferType;
    private Long originFarmId;
    private Long destinationFarmId;
    private String observations;
    private String transferDate;
    private List<CreateCattleTransferItemDto> items;
}
