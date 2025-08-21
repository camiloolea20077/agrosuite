package com.erp.backend_erp.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.inventory.CreateEstadosInventarioDto;
import com.erp.backend_erp.dto.inventory.EstadosInventarioDto;
import com.erp.backend_erp.dto.inventory.EstadosInventarioTableDto;
import com.erp.backend_erp.dto.inventory.UpdateEstadosInventarioDto;
import com.erp.backend_erp.entity.inventory.EstadosInventarioEntity;
import com.erp.backend_erp.mappers.estadosInventario.EstadosInventarioMapper;
import com.erp.backend_erp.repositories.estadosInventarios.EstadosInventarioJPARepository;
import com.erp.backend_erp.repositories.estadosInventarios.EstadosInventarioQueryRepository;
import com.erp.backend_erp.services.EstadosInventarioService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@Service
public class EstadosInventarioServiceImpl implements EstadosInventarioService {
    
    private final EstadosInventarioJPARepository estadosInventarioJPARepository;
    private final EstadosInventarioQueryRepository estadosInventarioQueryRepository;
    private final EstadosInventarioMapper estadosInventarioMapper;

    public EstadosInventarioServiceImpl(EstadosInventarioJPARepository estadosInventarioJPARepository,
                                    EstadosInventarioQueryRepository estadosInventarioQueryRepository,
                                    EstadosInventarioMapper estadosInventarioMapper) {
        this.estadosInventarioJPARepository = estadosInventarioJPARepository;
        this.estadosInventarioQueryRepository = estadosInventarioQueryRepository;
        this.estadosInventarioMapper = estadosInventarioMapper;
    }

    @Override
    public EstadosInventarioDto create(CreateEstadosInventarioDto createDto) {
        Boolean exists = estadosInventarioQueryRepository.existsByCodigo(createDto.getCodigo().toLowerCase());
        if (exists)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El código ya se encuentra registrado");
        try {
            EstadosInventarioEntity entity = estadosInventarioMapper.createToEntity(createDto);
            EstadosInventarioEntity savedEntity = estadosInventarioJPARepository.save(entity);
            return estadosInventarioMapper.toDto(savedEntity);
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<EstadosInventarioDto> createAll(List<CreateEstadosInventarioDto> dtoList) {
        List<EstadosInventarioDto> result = new ArrayList<>();
        for (CreateEstadosInventarioDto dto : dtoList) {
            try {
                EstadosInventarioDto saved = this.create(dto);
                result.add(saved);
            } catch (Exception e) {
                System.out.println("No se pudo guardar estado " + dto.getCodigo() + ": " + e.getMessage());
            }
        }
        return result;
    }

    @Override
    public Boolean update(UpdateEstadosInventarioDto updateDto) {
        EstadosInventarioEntity entity = estadosInventarioJPARepository.findById(updateDto.getId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        try {
            estadosInventarioMapper.updateEntityFromDto(updateDto, entity);
            estadosInventarioJPARepository.save(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el registro");
        }
    }

    @Override
    public Boolean delete(Long id) {
        estadosInventarioJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        estadosInventarioJPARepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean activate(Long id) {
        EstadosInventarioEntity entity = estadosInventarioJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        entity.setActivo(1L);
        estadosInventarioJPARepository.save(entity);
        return true;
    }

    @Override
    public EstadosInventarioDto findById(Long id) {
        EstadosInventarioEntity entity = estadosInventarioJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        return estadosInventarioMapper.toDto(entity);
    }

    @Override
    public List<EstadosInventarioDto> findAllActive() {
        List<EstadosInventarioEntity> entities = estadosInventarioJPARepository.findByActivoOrderByNombre(1L);
        return entities.stream().map(estadosInventarioMapper::toDto).toList();
    }

    @Override
    public PageImpl<EstadosInventarioTableDto> page(PageableDto<Object> pageableDto) {
        return estadosInventarioQueryRepository.pageEmployees(pageableDto);
    }
}