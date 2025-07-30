package com.erp.backend_erp.services.implementations;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.terceros.AutoCompleteDto;
import com.erp.backend_erp.dto.terceros.AutoCompleteModelDto;
import com.erp.backend_erp.dto.terceros.CreateTerceroDto;
import com.erp.backend_erp.dto.terceros.TerceroDto;
import com.erp.backend_erp.dto.terceros.UpdateTerceroDto;
import com.erp.backend_erp.entity.terceros.TercerosEntity;
import com.erp.backend_erp.mappers.terceros.TercerosMapper;
import com.erp.backend_erp.repositories.terceros.TercerosJPAQueryRepository;
import com.erp.backend_erp.repositories.terceros.TercerosQueryRepository;
import com.erp.backend_erp.services.TerceroService;
import com.erp.backend_erp.util.GlobalException;

@Service
public class TerceroServiceImpl  implements TerceroService {

    private final TercerosJPAQueryRepository tercerosJPAQueryRepository;
    private final TercerosMapper tercerosMapper;
    private final TercerosQueryRepository tercerosQueryRepository;
    public TerceroServiceImpl(TercerosJPAQueryRepository tercerosJPAQueryRepository,
        TercerosMapper tercerosMapper,
        TercerosQueryRepository tercerosQueryRepository) {
        this.tercerosJPAQueryRepository = tercerosJPAQueryRepository;
        this.tercerosMapper = tercerosMapper;
        this.tercerosQueryRepository = tercerosQueryRepository;
    }
    
    @Override
    public TerceroDto createTercero(CreateTerceroDto createTerceroDto) {
        Boolean exists = tercerosQueryRepository.existsTerceroByNumeroIdentificacion(createTerceroDto.getNumeroIdentificacion().toLowerCase());
        if (exists) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El Numero de identificacion ya se encuentra registrado");
        }
        try {
            TercerosEntity tercerosEntity = tercerosMapper.createToEntity(createTerceroDto);
            tercerosEntity.setFarmId(createTerceroDto.getFarmId());
            TercerosEntity savedTerceroEntity = tercerosJPAQueryRepository.save(tercerosEntity);
            return tercerosMapper.toDto(savedTerceroEntity);
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @Override
    public TerceroDto getTerceroById(Long id,Long farmId) {
        TercerosEntity tercerosEntity = tercerosJPAQueryRepository.findByIdAndFarmId(id, farmId)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        TerceroDto terceroDto = tercerosMapper.toDto(tercerosEntity);
        return terceroDto;
    }
    @Override
    public Boolean updateTercero(UpdateTerceroDto updateTerceroDto) {
        TercerosEntity tercerosEntity = tercerosJPAQueryRepository.findById(updateTerceroDto.getId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        try {
            tercerosMapper.updateEntityFromDto(updateTerceroDto, tercerosEntity);
            tercerosJPAQueryRepository.save(tercerosEntity);
            return true;
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @Override
    public List<AutoCompleteModelDto> autoCompleteTerceros(AutoCompleteDto<Object> autoCompleteDto, Long farmId) {
        return tercerosQueryRepository.autoCompleteTerceros(autoCompleteDto, farmId);
    }

}
