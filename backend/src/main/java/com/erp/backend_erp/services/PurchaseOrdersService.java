package com.erp.backend_erp.services;

import java.math.BigDecimal;
import java.util.List;

import com.erp.backend_erp.dto.purchaseOrders.CreatePurchaseOrdersDto;
import com.erp.backend_erp.dto.purchaseOrders.PurchaseOrderDetailsDto;
import com.erp.backend_erp.dto.purchaseOrders.PurchaseOrdersDto;
import com.erp.backend_erp.dto.purchaseOrders.UpdatePurchaseOrdersDto;

public interface PurchaseOrdersService {
    PurchaseOrdersDto create(CreatePurchaseOrdersDto createDto);
    Boolean update(UpdatePurchaseOrdersDto updateDto);
    Boolean delete(Long id);
    Boolean approve(Long id, Long userId);
    Boolean receive(Long id, Long userId);
    PurchaseOrdersDto findById(Long id, Long farmId);
    List<PurchaseOrdersDto> findBySupplier(Long supplierId, Long farmId);
    List<PurchaseOrdersDto> findPendingOrders(Long farmId);
    // PageImpl<PurchaseOrdersTableDto> getPage(PageableDto<Object> pageableDto);
    PurchaseOrdersDto addDetail(Long orderId, PurchaseOrderDetailsDto detailDto);
    Boolean removeDetail(Long orderId, Long detailId);
    Boolean updateDetail(Long detailId, PurchaseOrderDetailsDto detailDto);
    Boolean receivePartial(Long orderId, Long detailId, BigDecimal receivedQuantity, Long userId);
}