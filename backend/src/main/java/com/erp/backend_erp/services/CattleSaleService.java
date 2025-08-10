package com.erp.backend_erp.services;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.cattleSales.CattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.CreateCattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.OtpRequestInputDto;
import com.erp.backend_erp.dto.cattleSales.SalesTableDto;
import com.erp.backend_erp.dto.cattleSales.ViewCattleSaleDto;
import com.erp.backend_erp.util.PageableDto;

public interface  CattleSaleService {
    CattleSaleDto create(CreateCattleSaleDto dto);
    PageImpl<SalesTableDto> pageSales(PageableDto<Object> pageableDto);
    ViewCattleSaleDto findById(Long id, Long farmId);
    void confirmarVenta(Long ventaId, Long farmId);
    void anularVenta(Long ventaId, Long farmId);
    void solicitarAnulacionConOtp(Long saleId, Long farmId, OtpRequestInputDto dto) throws Exception;
    void autorizarAnulacionPorToken(String token);
}
