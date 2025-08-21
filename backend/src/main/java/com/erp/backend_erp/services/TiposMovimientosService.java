package com.erp.backend_erp.services;

import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.inventory.CreateTiposMovimientosDto;
import com.erp.backend_erp.dto.inventory.TiposMovimientosDto;
import com.erp.backend_erp.dto.inventory.TiposMovimientosTableDto;
import com.erp.backend_erp.dto.inventory.UpdateTiposMovimientosDto;
import com.erp.backend_erp.util.PageableDto;

public interface TiposMovimientosService {
    TiposMovimientosDto create(CreateTiposMovimientosDto createDto);
    List<TiposMovimientosDto> createAll(List<CreateTiposMovimientosDto> dtoList);
    Boolean update(UpdateTiposMovimientosDto updateDto);
    Boolean delete(Long id);
    Boolean activate(Long id);
    TiposMovimientosDto findById(Long id);
    List<TiposMovimientosDto> findAllActive();
    List<TiposMovimientosDto> findByType(Long esEntrada, Long esSalida);
    PageImpl<TiposMovimientosTableDto> page(PageableDto<Object> pageableDto);
}