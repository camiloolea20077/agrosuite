package com.erp.backend_erp.services;

import java.util.List;

import com.erp.backend_erp.dto.inventorySuppliers.CreateInventorySuppliersDto;
import com.erp.backend_erp.dto.inventorySuppliers.InventorySuppliersDto;
import com.erp.backend_erp.dto.inventorySuppliers.UpdateInventorySuppliersDto;

public interface InventorySuppliersService {
    InventorySuppliersDto create(CreateInventorySuppliersDto createDto);
    List<InventorySuppliersDto> createAll(List<CreateInventorySuppliersDto> dtoList);
    Boolean update(UpdateInventorySuppliersDto updateDto);
    Boolean delete(Long id);
    InventorySuppliersDto findById(Long id);
    List<InventorySuppliersDto> findByInventoryId(Long inventoryId);
    List<InventorySuppliersDto> findBySupplierId(Long supplierId);
    // PageImpl<InventorySuppliersTableDto> getPage(PageableDto<Object> pageableDto);
    InventorySuppliersDto findPrimarySupplier(Long inventoryId);
    Boolean setPrimarySupplier(Long inventoryId, Long supplierId);
}