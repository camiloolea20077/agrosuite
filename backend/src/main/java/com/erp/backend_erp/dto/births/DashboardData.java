package com.erp.backend_erp.dto.births;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardData {
    private List<DashboardBirthDto> births;  // Datos de nacimientos por mes
    private long totalCattle;  // Total de ganado
    private Long totalBirths;  // Total de nacimientos
    private Long totalEmployees;
    public DashboardData(List<DashboardBirthDto> births, long totalCattle, Long totalBirths, Long totalEmployees) {
        this.births = births;
        this.totalEmployees = totalEmployees;
        this.totalCattle = totalCattle;
        this.totalBirths = totalBirths;
    }
}
