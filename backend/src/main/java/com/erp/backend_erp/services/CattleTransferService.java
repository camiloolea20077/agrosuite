package com.erp.backend_erp.services;

import com.erp.backend_erp.dto.cattleTransfer.CattleTransferDto;
import com.erp.backend_erp.dto.cattleTransfer.CreateCattleTransferDto;

public interface CattleTransferService {
    CattleTransferDto createTransfer(CreateCattleTransferDto dto, Long userId, Long farmId);
    // ViewCattleTransferDto getTransferById(Long id);
}
