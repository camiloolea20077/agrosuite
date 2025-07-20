package com.erp.backend_erp.services.implementations;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.inventory.CreateInventoryDto;
import com.erp.backend_erp.dto.inventory.InventoryDto;
import com.erp.backend_erp.dto.inventory.InventoryTableDto;
import com.erp.backend_erp.dto.inventory.UpdateInventoryDto;
import com.erp.backend_erp.entity.inventory.InventoryEntity;
import com.erp.backend_erp.mappers.products.InventoryMapper;
import com.erp.backend_erp.repositories.products.InventoryJPARepository;
import com.erp.backend_erp.repositories.products.InventoryQueryRepository;
import com.erp.backend_erp.services.InventoryService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@Service
public class InventoryServiceImpl implements InventoryService {
    
    private final InventoryJPARepository productsJPARepository;
    private final InventoryMapper inventoryMapper;
    private final InventoryQueryRepository productsQueryRepository;
    public InventoryServiceImpl(InventoryJPARepository productsJPARepository , InventoryMapper inventoryMapper, InventoryQueryRepository productsQueryRepository) {
        this.inventoryMapper = inventoryMapper;
        this.productsQueryRepository = productsQueryRepository;
        this.productsJPARepository = productsJPARepository;
    }


    @Override
    public InventoryDto create(CreateInventoryDto createInventoryDto) {
        boolean exists = productsQueryRepository
            .existsByName(createInventoryDto.getNombre_insumo().toLowerCase());

        if (exists) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El nombre del producto ya se encuentra registrado");
        }

        try {
            InventoryEntity productEntity = inventoryMapper.createToEntity(createInventoryDto);
            productEntity.setFarmId(createInventoryDto.getFarmId());

            InventoryEntity savedProduct = productsJPARepository.save(productEntity);
            return inventoryMapper.toDto(savedProduct);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el producto: " + e.getMessage(), e);
        }
    }

    @Override
    public Boolean update(UpdateInventoryDto updateInventoryDto) {
        Optional<InventoryEntity> optionalInventory = productsJPARepository.findById(updateInventoryDto.getId());

        if (optionalInventory.isEmpty()) {
            throw new GlobalException(HttpStatus.NOT_FOUND, "El inventario con ID " + updateInventoryDto.getId() + " no existe.");
        }

        try {
            InventoryEntity entity = optionalInventory.get();

            entity.setNombreInsumo(updateInventoryDto.getNombre_insumo());
            entity.setUnidad(updateInventoryDto.getUnidad());
            entity.setCantidad_total(updateInventoryDto.getCantidad_total());
            entity.setDescripcion(updateInventoryDto.getDescripcion());
            entity.setUpdated_at(LocalDateTime.now());
            productsJPARepository.save(entity);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el producto: " + e.getMessage(), e);
        }
    }

    @Override
    public PageImpl<InventoryTableDto> pageInventory(PageableDto<Object> pageableDto) {
        return productsQueryRepository.pageInventory(pageableDto);
    }

    @Override
    public InventoryDto findById(Long id, Long farmId) {
        InventoryEntity inventoryEntity = productsJPARepository.findByIdAndFarmId(id, farmId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado o no pertenece a la finca"));
        return inventoryMapper.toDto(inventoryEntity);
    }
    
    @Override
    public Boolean delete(Long id) {
        productsJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        productsJPARepository.deleteById(id);
        return true;
    }


}
