package com.erp.backend_erp.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.suppliers.CreateSuppliersDto;
import com.erp.backend_erp.dto.suppliers.SuppliersDto;
import com.erp.backend_erp.dto.suppliers.UpdateSuppliersDto;
import com.erp.backend_erp.entity.inventory.SuppliersEntity;
import com.erp.backend_erp.mappers.suppliers.SuppliersMapper;
import com.erp.backend_erp.repositories.suppliers.SuppliersJPARepository;
import com.erp.backend_erp.repositories.suppliers.SuppliersQueryRepository;
import com.erp.backend_erp.services.SuppliersService;
import com.erp.backend_erp.util.GlobalException;

@Service
public class SuppliersServiceImpl implements SuppliersService {
    
    private final SuppliersJPARepository suppliersJPARepository;
    private final SuppliersQueryRepository suppliersQueryRepository;
    private final SuppliersMapper suppliersMapper;

    public SuppliersServiceImpl(SuppliersJPARepository suppliersJPARepository,
                            SuppliersQueryRepository suppliersQueryRepository,
                            SuppliersMapper suppliersMapper) {
        this.suppliersJPARepository = suppliersJPARepository;
        this.suppliersQueryRepository = suppliersQueryRepository;
        this.suppliersMapper = suppliersMapper;
    }

    @Override
    public SuppliersDto create(CreateSuppliersDto createDto) {
        if (createDto.getCodigo() != null) {
            Boolean exists = suppliersQueryRepository.existsByCodigo(createDto.getCodigo().toLowerCase());
            if (exists)
                throw new GlobalException(HttpStatus.BAD_REQUEST, "El código ya se encuentra registrado");
        }
        try {
            SuppliersEntity entity = suppliersMapper.createToEntity(createDto);
            SuppliersEntity savedEntity = suppliersJPARepository.save(entity);
            return suppliersMapper.toDto(savedEntity);
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<SuppliersDto> createAll(List<CreateSuppliersDto> dtoList) {
        List<SuppliersDto> result = new ArrayList<>();
        for (CreateSuppliersDto dto : dtoList) {
            try {
                SuppliersDto saved = this.create(dto);
                result.add(saved);
            } catch (Exception e) {
                System.out.println("No se pudo guardar proveedor " + dto.getNombre() + ": " + e.getMessage());
            }
        }
        return result;
    }

    @Override
    public Boolean update(UpdateSuppliersDto updateDto) {
        SuppliersEntity entity = suppliersJPARepository.findById(updateDto.getId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        try {
            suppliersMapper.updateEntityFromDto(updateDto, entity);
            suppliersJPARepository.save(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el registro");
        }
    }

    @Override
    public Boolean delete(Long id) {
        suppliersJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        suppliersJPARepository.deleteById(id);
        return true;
    }

    @Override
    public Boolean activate(Long id) {
        SuppliersEntity entity = suppliersJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        entity.setActivo(1L);
        suppliersJPARepository.save(entity);
        return true;
    }

    @Override
    public SuppliersDto findById(Long id) {
        SuppliersEntity entity = suppliersJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        return suppliersMapper.toDto(entity);
    }

    @Override
    public List<SuppliersDto> findAllActive() {
        List<SuppliersEntity> entities = suppliersJPARepository.findByActivoOrderByNombre(1L);
        return entities.stream().map(suppliersMapper::toDto).toList();
    }

    // @Override
    // public PageImpl<SuppliersTableDto> getPage(PageableDto<Object> pageableDto) {
    //     return suppliersQueryRepository.listSuppliers(pageableDto);
    // }
}