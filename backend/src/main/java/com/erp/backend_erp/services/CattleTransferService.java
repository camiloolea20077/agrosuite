package com.erp.backend_erp.services;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.cattleTransfer.CattleTransferDto;
import com.erp.backend_erp.dto.cattleTransfer.CattleTransferTableDto;
import com.erp.backend_erp.dto.cattleTransfer.CreateCattleTransferDto;
import com.erp.backend_erp.dto.cattleTransfer.ViewCattleTransferDto;
import com.erp.backend_erp.util.PageableDto;

public interface CattleTransferService {
    CattleTransferDto createTransfer(CreateCattleTransferDto dto, Long userId, Long farmId);
    ViewCattleTransferDto getTransferById(Long id, Long farmId);
    PageImpl<CattleTransferTableDto> pageTransfers(PageableDto<Object> pageableDto);
}
