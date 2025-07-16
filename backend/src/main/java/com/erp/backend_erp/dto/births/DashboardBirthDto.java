package com.erp.backend_erp.dto.births;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardBirthDto {
    private Integer month;  // Mes (1 - Enero, 2 - Febrero, etc.)
    private Long  maleCount;  // Nacimientos machos
    private Long  femaleCount;  // Nacimientos hembras
    
    public DashboardBirthDto(int month, Long  maleCount, Long  femaleCount) {
        this.month = month;
        this.maleCount = maleCount;
        this.femaleCount = femaleCount;
    }

}
