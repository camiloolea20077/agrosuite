package com.erp.backend_erp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.cattleSales.CattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.CreateCattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.SalesTableDto;
import com.erp.backend_erp.services.CattleSaleService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/cattle-sale")
public class CattleSaleController {
    @Autowired
    CattleSaleService cattleSaleService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createCattleSale(
            @RequestHeader("farmid") Long farmId,
            @Valid @RequestBody CreateCattleSaleDto createCattleSaleDto) throws Exception {
        try {
            createCattleSaleDto.setFarmId(farmId);
            CattleSaleDto savedSale = cattleSaleService.create(createCattleSaleDto);
            ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Registro de venta creado exitosamente",
                false,
                savedSale
            );
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @PostMapping("/page")
    public ResponseEntity<ApiResponse<Object>> pageSales(
		@RequestHeader("farmid") Long farmId,
            @Valid @RequestBody PageableDto<Object> pageableDto) {
        try {
			pageableDto.setFarmId(farmId);
            Page<SalesTableDto> employees = this.cattleSaleService.pageSales(pageableDto);
            if (employees.isEmpty())
                throw new GlobalException(HttpStatus.PARTIAL_CONTENT, "No se encontraron registros");
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, employees);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
