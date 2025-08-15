package com.erp.backend_erp.services;

import java.util.List;

import com.erp.backend_erp.dto.inventory.CreateEstadosMovimientosDto;
import com.erp.backend_erp.dto.inventory.EstadosMovimientosDto;
import com.erp.backend_erp.dto.inventory.UpdateEstadosMovimientosDto;

public interface EstadosMovimientosService {
    EstadosMovimientosDto create(CreateEstadosMovimientosDto createDto);
    List<EstadosMovimientosDto> createAll(List<CreateEstadosMovimientosDto> dtoList);
    Boolean update(UpdateEstadosMovimientosDto updateDto);
    Boolean delete(Long id);
    Boolean activate(Long id);
    EstadosMovimientosDto findById(Long id);
    List<EstadosMovimientosDto> findAllActive();
    // PageImpl<EstadosMovimientosTableDto> getPage(PageableDto<Object> pageableDto);
}