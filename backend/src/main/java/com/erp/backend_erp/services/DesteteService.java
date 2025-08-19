package com.erp.backend_erp.services;

import java.util.List;
import java.util.Map;

import com.erp.backend_erp.dto.births.DesteteTableDto;
import com.erp.backend_erp.dto.births.MigrarTerneroDto;
import com.erp.backend_erp.dto.births.ResultadoMigracionDto;

public interface DesteteService {
    List<DesteteTableDto> obtenerTernerosParaDestetar(Long farmId);
    List<DesteteTableDto> obtenerProximosDestetes(Long farmId, Integer limit);
    ResultadoMigracionDto migrarTernero(MigrarTerneroDto migrarDto, Long farmId);
    List<Map<String, Object>> obtenerHistorialDestetes(Long farmId);
}
