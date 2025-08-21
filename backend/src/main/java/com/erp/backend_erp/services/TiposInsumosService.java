package com.erp.backend_erp.services;

import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.inventory.CreateTiposInsumosDto;
import com.erp.backend_erp.dto.inventory.TiposInsumosDto;
import com.erp.backend_erp.dto.inventory.TiposInsumosTableDto;
import com.erp.backend_erp.dto.inventory.UpdateTiposInsumosDto;
import com.erp.backend_erp.util.PageableDto;

public interface TiposInsumosService {
    TiposInsumosDto create(CreateTiposInsumosDto createDto);
    List<TiposInsumosDto> createAll(List<CreateTiposInsumosDto> dtoList);
    Boolean update(UpdateTiposInsumosDto updateDto);
    Boolean delete(Long id);
    Boolean activate(Long id);
    TiposInsumosDto findById(Long id);
    List<TiposInsumosDto> findAllActive();
    PageImpl<TiposInsumosTableDto> page(PageableDto<Object> pageableDto);
}