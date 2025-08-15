package com.erp.backend_erp.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.backend_erp.dto.inventory.CreateTiposMovimientosDto;
import com.erp.backend_erp.dto.inventory.TiposMovimientosDto;
import com.erp.backend_erp.dto.inventory.UpdateTiposMovimientosDto;
import com.erp.backend_erp.entity.inventory.TiposMovimientosEntity;
import com.erp.backend_erp.mappers.tiposMovimientos.TiposMovimientosMapper;
import com.erp.backend_erp.repositories.tiposMovimientos.TiposMovimientosJPARepository;
import com.erp.backend_erp.repositories.tiposMovimientos.TiposMovimientosQueryRepository;
import com.erp.backend_erp.services.TiposMovimientosService;
import com.erp.backend_erp.util.GlobalException;

@Service
@Transactional
public class TiposMovimientosServiceImpl implements TiposMovimientosService {

    private final TiposMovimientosJPARepository tiposMovimientosJPARepository;
    private final TiposMovimientosQueryRepository tiposMovimientosQueryRepository;
    private final TiposMovimientosMapper tiposMovimientosMapper;

    public TiposMovimientosServiceImpl(TiposMovimientosJPARepository tiposMovimientosJPARepository,
                                       TiposMovimientosQueryRepository tiposMovimientosQueryRepository,
                                       TiposMovimientosMapper tiposMovimientosMapper) {
        this.tiposMovimientosJPARepository = tiposMovimientosJPARepository;
        this.tiposMovimientosQueryRepository = tiposMovimientosQueryRepository;
        this.tiposMovimientosMapper = tiposMovimientosMapper;
    }

    @Override
    public TiposMovimientosDto create(CreateTiposMovimientosDto createDto) {
        if (createDto.getCodigo() != null) {
            Boolean exists = tiposMovimientosQueryRepository.existsByCodigo(createDto.getCodigo().toLowerCase());
            if (exists) {
                throw new GlobalException(HttpStatus.BAD_REQUEST, "El código ya se encuentra registrado");
            }
        }
        try {
            TiposMovimientosEntity entity = tiposMovimientosMapper.createToEntity(createDto);
            TiposMovimientosEntity saved = tiposMovimientosJPARepository.save(entity);
            return tiposMovimientosMapper.toDto(saved);
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<TiposMovimientosDto> createAll(List<CreateTiposMovimientosDto> dtoList) {
        List<TiposMovimientosDto> result = new ArrayList<>();
        for (CreateTiposMovimientosDto dto : dtoList) {
            try {
                result.add(this.create(dto));
            } catch (Exception e) {
                System.out.println("No se pudo guardar tipo movimiento " + dto.getCodigo() + ": " + e.getMessage());
            }
        }
        return result;
    }

    @Override
    public Boolean update(UpdateTiposMovimientosDto updateDto) {
        TiposMovimientosEntity entity = tiposMovimientosJPARepository.findById(updateDto.getId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        try {
            tiposMovimientosMapper.updateEntityFromDto(updateDto, entity);
            tiposMovimientosJPARepository.save(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Error al actualizar el registro");
        }
    }

    @Override
    public Boolean delete(Long id) {
        tiposMovimientosJPARepository.findById(id)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        tiposMovimientosJPARepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean activate(Long id) {
        TiposMovimientosEntity entity = tiposMovimientosJPARepository.findById(id)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        // Ajusta el tipo si tu campo 'activo' no es Long:
        entity.setActivo(1L);
        tiposMovimientosJPARepository.save(entity);
        return true;
    }

    @Override
    public TiposMovimientosDto findById(Long id) {
        TiposMovimientosEntity entity = tiposMovimientosJPARepository.findById(id)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        return tiposMovimientosMapper.toDto(entity);
    }

    @Override
    public List<TiposMovimientosDto> findAllActive() {
        List<TiposMovimientosEntity> list = tiposMovimientosJPARepository.findByActivoOrderByNombre(1L);
        return list.stream().map(tiposMovimientosMapper::toDto).toList();
    }

    @Override
    public List<TiposMovimientosDto> findByType(Long esEntrada, Long esSalida) {
        List<TiposMovimientosEntity> list;
        final Long ACTIVO = 1L;

        if (esEntrada != null && esSalida != null) {
            list = tiposMovimientosJPARepository
                    .findByEsEntradaAndEsSalidaAndActivoOrderByNombre(esEntrada, esSalida, ACTIVO);
        } else if (esEntrada != null) {
            list = tiposMovimientosJPARepository
                    .findByEsEntradaAndActivoOrderByNombre(esEntrada, ACTIVO);
        } else if (esSalida != null) {
            list = tiposMovimientosJPARepository
                    .findByEsSalidaAndActivoOrderByNombre(esSalida, ACTIVO);
        } else {
            list = tiposMovimientosJPARepository.findByActivoOrderByNombre(ACTIVO);
        }
        return list.stream().map(tiposMovimientosMapper::toDto).toList();
    }
}