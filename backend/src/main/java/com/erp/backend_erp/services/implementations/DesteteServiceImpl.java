package com.erp.backend_erp.services.implementations;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.births.DesteteTableDto;
import com.erp.backend_erp.dto.births.MigrarTerneroDto;
import com.erp.backend_erp.dto.births.ResultadoMigracionDto;
import com.erp.backend_erp.repositories.births.BirthsQueryRepository;
import com.erp.backend_erp.services.DesteteService;

@Service
public class DesteteServiceImpl implements DesteteService {

    private final BirthsQueryRepository birthsQueryRepository;

    public DesteteServiceImpl(BirthsQueryRepository birthsQueryRepository) {
        this.birthsQueryRepository = birthsQueryRepository;
    }

    @Override
    public List<DesteteTableDto> obtenerTernerosParaDestetar(Long farmId) {
        return birthsQueryRepository.obtenerTernerosListosParaDestetar(farmId);
    }

    @Override
    public List<DesteteTableDto> obtenerProximosDestetes(Long farmId, Integer limit) {
        return birthsQueryRepository.obtenerProximosDestetes(farmId, limit);
    }

    @Override
    public ResultadoMigracionDto migrarTernero(MigrarTerneroDto migrarDto, Long farmId) {
        return birthsQueryRepository.migrarTerneroACattle(
            migrarDto.getBirthId(), 
            migrarDto.getDecision()
        );
    }

    @Override
    public List<Map<String, Object>> obtenerHistorialDestetes(Long farmId) {
        return birthsQueryRepository.obtenerHistorialDestetes(farmId);
    }
}
