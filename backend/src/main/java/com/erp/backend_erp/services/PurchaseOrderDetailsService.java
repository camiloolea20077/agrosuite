package com.erp.backend_erp.services;

import java.util.List;

import com.erp.backend_erp.dto.purchaseOrders.CreatePurchaseOrderDetailsDto;
import com.erp.backend_erp.dto.purchaseOrders.PurchaseOrderDetailsDto;
import com.erp.backend_erp.dto.purchaseOrders.UpdatePurchaseOrderDetailsDto;

public interface PurchaseOrderDetailsService {
    PurchaseOrderDetailsDto create(CreatePurchaseOrderDetailsDto createDto);
    List<PurchaseOrderDetailsDto> createAll(List<CreatePurchaseOrderDetailsDto> dtoList);
    Boolean update(UpdatePurchaseOrderDetailsDto updateDto);
    Boolean delete(Long id);
    PurchaseOrderDetailsDto findById(Long id);
    List<PurchaseOrderDetailsDto> findByOrderId(Long orderId);
    // PageImpl<PurchaseOrderDetailsTableDto> getPage(PageableDto<Object> pageableDto);
}