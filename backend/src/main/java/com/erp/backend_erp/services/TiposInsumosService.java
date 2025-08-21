package com.erp.backend_erp.services;

import java.util.List;

import com.erp.backend_erp.dto.inventory.CreateTiposInsumosDto;
import com.erp.backend_erp.dto.inventory.TiposInsumosDto;
import com.erp.backend_erp.dto.inventory.UpdateTiposInsumosDto;

public interface TiposInsumosService {
    TiposInsumosDto create(CreateTiposInsumosDto createDto);
    List<TiposInsumosDto> createAll(List<CreateTiposInsumosDto> dtoList);
    Boolean update(UpdateTiposInsumosDto updateDto);
    Boolean delete(Long id);
    Boolean activate(Long id);
    TiposInsumosDto findById(Long id);
    List<TiposInsumosDto> findAllActive();
}