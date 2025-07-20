package com.erp.backend_erp.services;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.inventory.CreateInventoryDto;
import com.erp.backend_erp.dto.inventory.InventoryDto;
import com.erp.backend_erp.dto.inventory.InventoryTableDto;
import com.erp.backend_erp.dto.inventory.UpdateInventoryDto;
import com.erp.backend_erp.util.PageableDto;


public interface InventoryService {
    InventoryDto create(CreateInventoryDto createInventoryDto);
    Boolean update(UpdateInventoryDto updateInventoryDto);
    PageImpl<InventoryTableDto> pageInventory(PageableDto<Object> pageableDto);
    InventoryDto findById(Long id, Long farmId);
    Boolean delete(Long id);
}
