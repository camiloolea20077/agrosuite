package com.erp.backend_erp.services;

import java.util.List;

import com.erp.backend_erp.dto.terceros.AutoCompleteDto;
import com.erp.backend_erp.dto.terceros.AutoCompleteModelDto;
import com.erp.backend_erp.dto.terceros.CreateTerceroDto;
import com.erp.backend_erp.dto.terceros.TerceroDto;
import com.erp.backend_erp.dto.terceros.UpdateTerceroDto;

public interface TerceroService {
    TerceroDto createTercero(CreateTerceroDto createTerceroDto);

    TerceroDto getTerceroById(Long id, Long farmId);

    Boolean updateTercero(UpdateTerceroDto updateTerceroDto);

    List<AutoCompleteModelDto> autoCompleteTerceros(AutoCompleteDto<Object> autoCompleteDto, Long farmId);

}
