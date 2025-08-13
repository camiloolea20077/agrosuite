package com.erp.backend_erp.dto.cattleTransfer;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCattleTransferItemDto {
    private Long cattleId;
    private Long birthId;
    private String numero_ganado;
    private BigDecimal peso;
}
