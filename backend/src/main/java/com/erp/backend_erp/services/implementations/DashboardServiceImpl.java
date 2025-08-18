package com.erp.backend_erp.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.births.DesteteTableDto;
import com.erp.backend_erp.repositories.births.BirthsQueryRepository;
import com.erp.backend_erp.services.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {


    private final BirthsQueryRepository birthsQueryRepository;

    public DashboardServiceImpl(BirthsQueryRepository birthsQueryRepository) {
        this.birthsQueryRepository = birthsQueryRepository;
    }
    
    @Override
    public List<DesteteTableDto> listDesteteDashboard(Long farmId) {
        return birthsQueryRepository.listDesteteDashboard(farmId);
    }


}
