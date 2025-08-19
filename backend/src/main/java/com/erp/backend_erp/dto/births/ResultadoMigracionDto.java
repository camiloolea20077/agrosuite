package com.erp.backend_erp.dto.births;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultadoMigracionDto {
    private Boolean success;
    private String message;
    private Long birthId;
    private Long cattleId;
    private String decision;

    public ResultadoMigracionDto(boolean success, String message, Long birthId, Long cattleId, String decision) {
    this.success = success;
    this.message = message;
    this.birthId = birthId;
    this.cattleId = cattleId;
    this.decision = decision;
}
}
