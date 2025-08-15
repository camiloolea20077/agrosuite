package com.erp.backend_erp.services;

import java.util.List;

import com.erp.backend_erp.dto.insumos.CreateInsumoRequestDetailsDto;
import com.erp.backend_erp.dto.insumos.InsumoRequestDetailsDto;
import com.erp.backend_erp.dto.insumos.UpdateInsumoRequestDetailsDto;

public interface InsumoRequestDetailsService {
    InsumoRequestDetailsDto create(CreateInsumoRequestDetailsDto createDto);
    List<InsumoRequestDetailsDto> createAll(List<CreateInsumoRequestDetailsDto> dtoList);
    Boolean update(UpdateInsumoRequestDetailsDto updateDto);
    Boolean delete(Long id);
    InsumoRequestDetailsDto findById(Long id);
    List<InsumoRequestDetailsDto> findByRequestId(Long requestId);
    // PageImpl<InsumoRequestDetailsTableDto> getPage(PageableDto<Object> pageableDto);
}
