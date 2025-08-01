package com.erp.backend_erp.services;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.cattleSales.CattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.CreateCattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.SalesTableDto;
import com.erp.backend_erp.dto.cattleSales.ViewCattleSaleDto;
import com.erp.backend_erp.util.PageableDto;

public interface  CattleSaleService {
    CattleSaleDto create(CreateCattleSaleDto dto);
    PageImpl<SalesTableDto> pageSales(PageableDto<Object> pageableDto);
    ViewCattleSaleDto findById(Long id, Long farmId);
}
