package com.erp.backend_erp.services;

import java.util.List;

import com.erp.backend_erp.dto.births.DesteteTableDto;

public interface DashboardService {
    List<DesteteTableDto> listDesteteDashboard(Long farmId);
}
