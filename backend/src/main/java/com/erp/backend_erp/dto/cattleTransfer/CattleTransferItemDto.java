package com.erp.backend_erp.dto.cattleTransfer;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CattleTransferItemDto {
    private Long id;
    private Long transferId;
    private Long cattleId;
    private Long birthId;
    private String numero_ganado;
    private BigDecimal peso; // o pesoTransfer si quieres ser más específico
}
