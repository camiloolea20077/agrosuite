package com.erp.backend_erp.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.InventoryMovements.CreateInventoryMovementsDto;
import com.erp.backend_erp.dto.InventoryMovements.InventoryMovementsDto;
import com.erp.backend_erp.dto.InventoryMovements.InventoryMovementsTableDto;
import com.erp.backend_erp.dto.InventoryMovements.UpdateInventoryMovementsDto;
import com.erp.backend_erp.util.PageableDto;

public interface InventoryMovementsService {
    InventoryMovementsDto create(CreateInventoryMovementsDto createDto);
    List<InventoryMovementsDto> createAll(List<CreateInventoryMovementsDto> dtoList);
    Boolean update(UpdateInventoryMovementsDto updateDto);
    Boolean delete(Long id);
    Boolean approve(Long id, Long userId);
    Boolean close(Long id, Long userId);
    InventoryMovementsDto findById(Long id, Long farmId);
    List<InventoryMovementsDto> findByInventory(Long inventoryId, Long farmId);
    List<InventoryMovementsDto> findByDateRange(Long farmId, LocalDateTime startDate, LocalDateTime endDate);
    List<InventoryMovementsDto> findPendingApprovals(Long farmId);
    InventoryMovementsDto processEntry(CreateInventoryMovementsDto createDto);
    InventoryMovementsDto processExit(CreateInventoryMovementsDto createDto);
    Boolean processReturn(Long movementId, BigDecimal returnQuantity, Long userId);
    List<InventoryMovementsDto> findByInventoryAndFarm(Long inventoryId, Long farmId);
    List<InventoryMovementsDto> findByFarmAndDateRange(Long farmId, LocalDateTime startDate, LocalDateTime endDate);
    List<InventoryMovementsDto> listPendingApprovals(Long farmId);
    Boolean reject(Long id, Long approverUserId, String reason);
    PageImpl<InventoryMovementsTableDto> getPage(PageableDto<Object> pageableDto);
}