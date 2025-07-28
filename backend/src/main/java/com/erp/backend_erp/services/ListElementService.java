package com.erp.backend_erp.services;

import java.util.List;

import com.erp.backend_erp.dto.listElements.CattleElementsDto;
import com.erp.backend_erp.dto.listElements.FarmsElementsDto;
import com.erp.backend_erp.dto.listElements.RolesElementsDto;

public interface ListElementService {
    List<CattleElementsDto> findListForId(Long farmId);
    List<CattleElementsDto> findListForIdFemale(Long farmId);
    List<FarmsElementsDto> getFarms();
    List<RolesElementsDto> getRoles();
}
