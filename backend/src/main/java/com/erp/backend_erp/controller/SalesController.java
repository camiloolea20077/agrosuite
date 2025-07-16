package com.erp.backend_erp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.sales.CreateSaleDto;
import com.erp.backend_erp.dto.sales.SaleDto;
import com.erp.backend_erp.services.SalesService;
import com.erp.backend_erp.util.ApiResponse;

@RestController
@RequestMapping("/sales")
public class SalesController {

    
    @Autowired
    private SalesService salesService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createSale(@Valid @RequestBody CreateSaleDto createSaleDto) {
        try {
            SaleDto savedSale = salesService.createSale(createSaleDto);
            ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Venta registrada exitosamente",
                false,
                savedSale
            );
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
