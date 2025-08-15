package com.erp.backend_erp.services.implementations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.inventory.CreateInventoryDto;
import com.erp.backend_erp.dto.inventory.InventoryDto;
import com.erp.backend_erp.dto.inventory.InventoryTableDto;
import com.erp.backend_erp.dto.inventory.ListInventoryDto;
import com.erp.backend_erp.dto.inventory.UpdateInventoryDto;
import com.erp.backend_erp.entity.inventory.InventoryEntity;
import com.erp.backend_erp.mappers.inventory.InventoryMapper;
import com.erp.backend_erp.repositories.products.InventoryJPARepository;
import com.erp.backend_erp.repositories.products.InventoryQueryRepository;
import com.erp.backend_erp.services.InventoryService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@Service
public class InventoryServiceImpl implements InventoryService {
    
    private final InventoryJPARepository inventoryJPARepository;
    private final InventoryQueryRepository inventoryQueryRepository;
    private final InventoryMapper inventoryMapper;

    public InventoryServiceImpl(InventoryJPARepository inventoryJPARepository,
                            InventoryQueryRepository inventoryQueryRepository,
                            InventoryMapper inventoryMapper) {
        this.inventoryJPARepository = inventoryJPARepository;
        this.inventoryQueryRepository = inventoryQueryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public InventoryDto create(CreateInventoryDto createDto) {
        if (createDto.getCodigoInterno() != null) {
            Boolean exists = inventoryQueryRepository.existsByCodigoInterno(createDto.getCodigoInterno().toLowerCase());
            if (exists)
                throw new GlobalException(HttpStatus.BAD_REQUEST, "El código interno ya se encuentra registrado");
        }
        try {
            InventoryEntity entity = inventoryMapper.createToEntity(createDto);
            entity.setFarmId(createDto.getFarmId());
            InventoryEntity savedEntity = inventoryJPARepository.save(entity);
            return inventoryMapper.toDto(savedEntity);
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    public List<InventoryDto> createAll(List<CreateInventoryDto> dtoList) {
        List<InventoryDto> result = new ArrayList<>();
        for (CreateInventoryDto dto : dtoList) {
            try {
                InventoryDto saved = this.create(dto);
                result.add(saved);
            } catch (Exception e) {
                System.out.println("No se pudo guardar inventario " + dto.getNombreInsumo() + ": " + e.getMessage());
            }
        }
        return result;
    }

    @Override
    public Boolean update(UpdateInventoryDto updateDto) {
        InventoryEntity entity = inventoryJPARepository.findById(updateDto.getId())
                .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        try {
            inventoryMapper.updateEntityFromDto(updateDto, entity);
            inventoryJPARepository.save(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el registro");
        }
    }

    @Override
    public Boolean delete(Long id) {
        inventoryJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        inventoryJPARepository.deleteById(id);
        return true;
    }

    @Override
    public InventoryDto findById(Long id, Long farmId) {
        InventoryEntity entity = inventoryJPARepository.findByIdAndFarmId(id, farmId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        return inventoryMapper.toDto(entity);
    }

    @Override
    public List<InventoryDto> findByFarm(Long farmId) {
        List<InventoryEntity> entities = inventoryJPARepository.findByFarmIdOrderByNombreInsumo(farmId);
        return entities.stream().map(inventoryMapper::toDto).toList();
    }

    // @Override
    // public List<InventoryStockDto> getLowStockItems(Long farmId) {
    //     return inventoryQueryRepository.getLowStockItems(farmId);
    // }

    // @Override
    // public List<InventoryStockDto> getCriticalStockItems(Long farmId) {
    //     return inventoryQueryRepository.getCriticalStockItems(farmId);
    // }

    @Override
    public PageImpl<InventoryTableDto> getPage(PageableDto<Object> pageableDto) {
        return inventoryQueryRepository.pageInventory(pageableDto);
    }

    @Override
    public List<ListInventoryDto> getInventory(Long farmId) {
        List<ListInventoryDto> elements = inventoryQueryRepository.getInventory(farmId);
        if (elements.isEmpty()) {
            throw new GlobalException(HttpStatus.OK, "No se encontraron registros");
        }
        return elements;
    }


    @Override
    public Boolean updateStock(Long inventoryId, BigDecimal newQuantity, Long userId) {
        InventoryEntity entity = inventoryJPARepository.findById(inventoryId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Inventario no encontrado"));
        
        entity.setCantidadActual(newQuantity);
        entity.setUpdatedBy(userId);
        inventoryJPARepository.save(entity);
        return true;
    }

    @Override
    public Boolean reserveStock(Long inventoryId, BigDecimal quantity, Long userId) {
        InventoryEntity entity = inventoryJPARepository.findById(inventoryId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Inventario no encontrado"));
        
        BigDecimal availableQuantity = entity.getCantidadActual().subtract(entity.getCantidadReservada());
        if (availableQuantity.compareTo(quantity) < 0) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "Stock insuficiente para reservar");
        }
        
        entity.setCantidadReservada(entity.getCantidadReservada().add(quantity));
        entity.setUpdatedBy(userId);
        inventoryJPARepository.save(entity);
        return true;
    }

    @Override
    public Boolean releaseReservedStock(Long inventoryId, BigDecimal quantity, Long userId) {
        InventoryEntity entity = inventoryJPARepository.findById(inventoryId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Inventario no encontrado"));
        
        if (entity.getCantidadReservada().compareTo(quantity) < 0) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "No hay suficiente stock reservado");
        }
        
        entity.setCantidadReservada(entity.getCantidadReservada().subtract(quantity));
        entity.setUpdatedBy(userId);
        inventoryJPARepository.save(entity);
        return true;
    }
}