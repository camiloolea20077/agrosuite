package com.erp.backend_erp.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.inventory.CreateInventoryDto;
import com.erp.backend_erp.dto.inventory.InventoryDto;
import com.erp.backend_erp.dto.inventory.InventoryTableDto;
import com.erp.backend_erp.dto.inventory.ListInventoryDto;
import com.erp.backend_erp.dto.inventory.UpdateInventoryDto;
import com.erp.backend_erp.util.PageableDto;

public interface InventoryService {
    InventoryDto create(CreateInventoryDto createDto);
    List<InventoryDto> createAll(List<CreateInventoryDto> dtoList);
    Boolean update(UpdateInventoryDto updateDto);
    Boolean delete(Long id);
    InventoryDto findById(Long id, Long farmId);
    List<InventoryDto> findByFarm(Long farmId);
    // List<InventoryStockDto> getLowStockItems(Long farmId);
    // List<InventoryStockDto> getCriticalStockItems(Long farmId);
    PageImpl<InventoryTableDto> getPage(PageableDto<Object> pageableDto);
    // InventoryReportDto getInventoryReport(Long farmId);
    Boolean updateStock(Long inventoryId, BigDecimal newQuantity, Long userId);
    Boolean reserveStock(Long inventoryId, BigDecimal quantity, Long userId);
    Boolean releaseReservedStock(Long inventoryId, BigDecimal quantity, Long userId);
    List<ListInventoryDto> getInventory(Long farmId);
}