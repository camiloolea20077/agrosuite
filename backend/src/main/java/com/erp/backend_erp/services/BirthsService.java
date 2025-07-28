package com.erp.backend_erp.services;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.births.BirthsDto;
import com.erp.backend_erp.dto.births.BirthsTableDto;
import com.erp.backend_erp.dto.births.CreateBirthsDto;
import com.erp.backend_erp.dto.births.DashboardData;
import com.erp.backend_erp.dto.births.UpdateBirthsDto;
import com.erp.backend_erp.util.PageableDto;

public interface BirthsService {
    BirthsDto create(CreateBirthsDto createBirthsDto);
    Boolean update(UpdateBirthsDto updateBirthsDto);
    Boolean delete(Long id);
    PageImpl<BirthsTableDto> pageGanado(PageableDto<Object> birthsTableDto);
    BirthsDto findById(Long id, Long farmId);
    DashboardData getDashboardData(Long farmId);
}
