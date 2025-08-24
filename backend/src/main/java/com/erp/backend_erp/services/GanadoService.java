package com.erp.backend_erp.services;

import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.ganado.CreateGanadoDto;
import com.erp.backend_erp.dto.ganado.GanadoDto;
import com.erp.backend_erp.dto.ganado.GanadoListDto;
import com.erp.backend_erp.dto.ganado.GanadoTableDto;
import com.erp.backend_erp.dto.ganado.UpdateGanadoDto;
import com.erp.backend_erp.util.PageableDto;

public interface  GanadoService {
    GanadoDto create(CreateGanadoDto createGanadoDto);
    Boolean update(UpdateGanadoDto updateGanadoDto);
    Boolean delete(Long id);
    PageImpl<GanadoTableDto> pageGanado(PageableDto<Object> pageableDto);
    List<GanadoDto> createAll(List<CreateGanadoDto> dtoList);
    GanadoDto findById(Long id, Long farmId);
    List<GanadoListDto> getGanado(Long farmId);
}
