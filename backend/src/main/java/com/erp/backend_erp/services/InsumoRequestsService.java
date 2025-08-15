package com.erp.backend_erp.services;

import java.util.List;

import com.erp.backend_erp.dto.insumos.CreateInsumoRequestsDto;
import com.erp.backend_erp.dto.insumos.InsumoRequestDetailsDto;
import com.erp.backend_erp.dto.insumos.InsumoRequestsDto;
import com.erp.backend_erp.dto.insumos.UpdateInsumoRequestsDto;

public interface InsumoRequestsService {
    InsumoRequestsDto create(CreateInsumoRequestsDto createDto);
    Boolean update(UpdateInsumoRequestsDto updateDto);
    Boolean delete(Long id);
    Boolean approve(Long id, Long userId);
    Boolean reject(Long id, Long userId, String reason);
    InsumoRequestsDto findById(Long id, Long farmId);
    List<InsumoRequestsDto> findByRequesterId(Long requesterId, Long farmId);
    List<InsumoRequestsDto> findPendingRequests(Long farmId);
    InsumoRequestsDto addDetail(Long requestId, InsumoRequestDetailsDto detailDto);
    Boolean removeDetail(Long requestId, Long detailId);
    Boolean updateDetail(Long detailId, InsumoRequestDetailsDto detailDto);
}
