package com.erp.backend_erp.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.inventory.CreateTiposInsumosDto;
import com.erp.backend_erp.dto.inventory.TiposInsumosDto;
import com.erp.backend_erp.dto.inventory.UpdateTiposInsumosDto;
import com.erp.backend_erp.entity.inventory.TiposInsumosEntity;
import com.erp.backend_erp.mappers.tiposInsumos.TiposInsumosMapper;
import com.erp.backend_erp.repositories.tiposInsumos.TiposInsumosJPARepository;
import com.erp.backend_erp.repositories.tiposInsumos.TiposInsumosQueryRepository;
import com.erp.backend_erp.services.TiposInsumosService;
import com.erp.backend_erp.util.GlobalException;

@Service
public class TiposInsumosServiceImpl implements TiposInsumosService {
    
    private final TiposInsumosJPARepository tiposInsumosJPARepository;
    private final TiposInsumosQueryRepository tiposInsumosQueryRepository;
    private final TiposInsumosMapper tiposInsumosMapper;

    public TiposInsumosServiceImpl(TiposInsumosJPARepository tiposInsumosJPARepository,
                                TiposInsumosQueryRepository tiposInsumosQueryRepository,
                                TiposInsumosMapper tiposInsumosMapper) {
        this.tiposInsumosJPARepository = tiposInsumosJPARepository;
        this.tiposInsumosQueryRepository = tiposInsumosQueryRepository;
        this.tiposInsumosMapper = tiposInsumosMapper;
    }

    @Override
    public TiposInsumosDto create(CreateTiposInsumosDto createDto) {
        Boolean exists = tiposInsumosQueryRepository.existsByCodigo(createDto.getCodigo().toLowerCase());
        if (exists)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El código ya se encuentra registrado");
        try {
            TiposInsumosEntity entity = tiposInsumosMapper.createToEntity(createDto);
            TiposInsumosEntity savedEntity = tiposInsumosJPARepository.save(entity);
            return tiposInsumosMapper.toDto(savedEntity);
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<TiposInsumosDto> createAll(List<CreateTiposInsumosDto> dtoList) {
        List<TiposInsumosDto> result = new ArrayList<>();
        for (CreateTiposInsumosDto dto : dtoList) {
            try {
                TiposInsumosDto saved = this.create(dto);
                result.add(saved);
            } catch (Exception e) {
                System.out.println("No se pudo guardar tipo insumo " + dto.getCodigo() + ": " + e.getMessage());
            }
        }
        return result;
    }

    @Override
    public Boolean update(UpdateTiposInsumosDto updateDto) {
        TiposInsumosEntity entity = tiposInsumosJPARepository.findById(updateDto.getId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        try {
            tiposInsumosMapper.updateEntityFromDto(updateDto, entity);
            tiposInsumosJPARepository.save(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el registro");
        }
    }

    @Override
    public Boolean delete(Long id) {
        tiposInsumosJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        tiposInsumosJPARepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean activate(Long id) {
        TiposInsumosEntity entity = tiposInsumosJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        entity.setActivo(1L);
        tiposInsumosJPARepository.save(entity);
        return true;
    }

    @Override
    public TiposInsumosDto findById(Long id) {
        TiposInsumosEntity entity = tiposInsumosJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        return tiposInsumosMapper.toDto(entity);
    }

    @Override
    public List<TiposInsumosDto> findAllActive() {
        List<TiposInsumosEntity> entities = tiposInsumosJPARepository.findByActivoOrderByNombre(1L);
        return entities.stream().map(tiposInsumosMapper::toDto).toList();
    }
}