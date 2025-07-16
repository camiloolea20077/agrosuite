package com.erp.backend_erp.services;

import com.erp.backend_erp.dto.sales.CreateSaleDto;
import com.erp.backend_erp.dto.sales.SaleDto;

public interface SalesService {
    SaleDto createSale(CreateSaleDto createSaleDto);
}
