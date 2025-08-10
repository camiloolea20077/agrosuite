package com.erp.backend_erp.dto.cattleTransfer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CattleTransferItemDto {
    private Long id;
    private Long transferId;
    private Long cattleId;
    private Long birthId;
}
