package com.erp.backend_erp.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.backend_erp.dto.inventory.CreateEstadosMovimientosDto;
import com.erp.backend_erp.dto.inventory.EstadosMovimientosDto;
import com.erp.backend_erp.dto.inventory.UpdateEstadosMovimientosDto;
import com.erp.backend_erp.entity.inventory.EstadosMovimientosEntity;
import com.erp.backend_erp.mappers.tiposMovimientos.EstadosMovimientosMapper;
import com.erp.backend_erp.repositories.estadosMovimientos.EstadosMovimientosJPARepository;
import com.erp.backend_erp.repositories.estadosMovimientos.EstadosMovimientosQueryRepository;
import com.erp.backend_erp.services.EstadosMovimientosService;
import com.erp.backend_erp.util.GlobalException;

@Service
@Transactional
public class EstadosMovimientosServiceImpl implements EstadosMovimientosService {

    private final EstadosMovimientosJPARepository jpa;
    private final EstadosMovimientosQueryRepository qry;
    private final EstadosMovimientosMapper mapper;

    public EstadosMovimientosServiceImpl(EstadosMovimientosJPARepository jpa,
                                        EstadosMovimientosQueryRepository qry,
                                        EstadosMovimientosMapper mapper) {
        this.jpa = jpa;
        this.qry = qry;
        this.mapper = mapper;
    }

    @Override
    public EstadosMovimientosDto create(CreateEstadosMovimientosDto createDto) {
        // Validar código único (si se envía)
        if (createDto.getCodigo() != null && !createDto.getCodigo().isBlank()) {
            Boolean exists = qry.existsByCodigo(createDto.getCodigo().toLowerCase());
            if (exists) {
                throw new GlobalException(HttpStatus.BAD_REQUEST, "El código ya se encuentra registrado");
            }
        }

        try {
            EstadosMovimientosEntity entity = mapper.createToEntity(createDto);

            // Defaults por si llegan nulos
            if (entity.getActivo() == null) entity.setActivo(1L);

            EstadosMovimientosEntity saved = jpa.save(entity);
            return mapper.toDto(saved);
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<EstadosMovimientosDto> createAll(List<CreateEstadosMovimientosDto> dtoList) {
        List<EstadosMovimientosDto> result = new ArrayList<>();
        for (CreateEstadosMovimientosDto dto : dtoList) {
            try {
                result.add(this.create(dto));
            } catch (Exception e) {
                // Puedes loggear si prefieres no usar System.out
                System.out.println("No se pudo guardar estado " + dto.getCodigo() + ": " + e.getMessage());
            }
        }
        return result;
    }

    @Override
    public Boolean update(UpdateEstadosMovimientosDto updateDto) {
        EstadosMovimientosEntity entity = jpa.findById(updateDto.getId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        try {
            mapper.updateEntityFromDto(updateDto, entity);
            jpa.save(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Error al actualizar el registro");
        }
    }

    @Override
    public Boolean delete(Long id) {
        jpa.findById(id).orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        jpa.deleteById(id); // si tu entity tiene @SQLDelete, marcará deleted_at
        return true;
    }

    @Override
    public Boolean activate(Long id) {
        EstadosMovimientosEntity entity = jpa.findById(id)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        entity.setActivo(1L);
        jpa.save(entity);
        return true;
    }

    @Override
    public EstadosMovimientosDto findById(Long id) {
        EstadosMovimientosEntity entity = jpa.findById(id)
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        return mapper.toDto(entity);
    }

    @Override
    public List<EstadosMovimientosDto> findAllActive() {
        // Cambia el tipo si tu 'activo' es Short/Boolean en la entity
        List<EstadosMovimientosEntity> list = jpa.findByActivoOrderByNombre(1L);
        return list.stream().map(mapper::toDto).toList();
    }
}