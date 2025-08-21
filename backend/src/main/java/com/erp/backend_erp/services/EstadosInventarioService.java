package com.erp.backend_erp.services;

import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.inventory.CreateEstadosInventarioDto;
import com.erp.backend_erp.dto.inventory.EstadosInventarioDto;
import com.erp.backend_erp.dto.inventory.EstadosInventarioTableDto;
import com.erp.backend_erp.dto.inventory.UpdateEstadosInventarioDto;
import com.erp.backend_erp.util.PageableDto;

public interface EstadosInventarioService {
    EstadosInventarioDto create(CreateEstadosInventarioDto createDto);
    List<EstadosInventarioDto> createAll(List<CreateEstadosInventarioDto> dtoList);
    Boolean update(UpdateEstadosInventarioDto updateDto);
    Boolean delete(Long id);
    Boolean activate(Long id);
    EstadosInventarioDto findById(Long id);
    List<EstadosInventarioDto> findAllActive();
    PageImpl<EstadosInventarioTableDto> page(PageableDto<Object> pageableDto);
}
