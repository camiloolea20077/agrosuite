package com.erp.backend_erp.services;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.cattleDeath.CattleDeathDto;
import com.erp.backend_erp.dto.cattleDeath.CreateCattleDeathDto;
import com.erp.backend_erp.dto.cattleDeath.DeathsTableDto;
import com.erp.backend_erp.dto.cattleDeath.ViewCattleDeathDto;
import com.erp.backend_erp.util.PageableDto;

public interface CattleDeathService {
    CattleDeathDto create(CreateCattleDeathDto dto);
    PageImpl<DeathsTableDto> pageDeaths(PageableDto<Object> pageableDto);
    ViewCattleDeathDto findById(Integer id, Long farmId);
    CattleDeathDto findByIds(Integer id, Long farmId);
}
