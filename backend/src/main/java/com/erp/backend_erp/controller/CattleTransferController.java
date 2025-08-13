package com.erp.backend_erp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.cattleTransfer.CattleTransferDto;
import com.erp.backend_erp.dto.cattleTransfer.CattleTransferTableDto;
import com.erp.backend_erp.dto.cattleTransfer.CreateCattleTransferDto;
import com.erp.backend_erp.dto.cattleTransfer.ViewCattleTransferDto;
import com.erp.backend_erp.services.CattleTransferService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/cattle-transfer")
public class CattleTransferController {
    @Autowired
    CattleTransferService cattleTransferService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createCattleTransfer(
            @RequestHeader("farmid") Long farmId,
            @RequestHeader("user") Long userId,
            @Valid @RequestBody CreateCattleTransferDto createCattleTransferDto) {
        try {
            // El servicio asigna el farmId y userId manualmente
            CattleTransferDto saved = cattleTransferService.createTransfer(createCattleTransferDto, userId, farmId);

            ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Traslado registrado exitosamente",
                false,
                saved
            );
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex; // Puedes personalizar esto con un handler global si lo deseas
        }
    }
    @PostMapping("/page")
    public ResponseEntity<ApiResponse<Object>> pageSales(
		@RequestHeader("farmid") Long farmId,
            @Valid @RequestBody PageableDto<Object> pageableDto) {
        try {
			pageableDto.setFarmId(farmId);
            Page<CattleTransferTableDto> transfers = this.cattleTransferService.pageTransfers(pageableDto);
            if (transfers.isEmpty())
                throw new GlobalException(HttpStatus.PARTIAL_CONTENT, "No se encontraron registros");
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, transfers);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ViewCattleTransferDto>> findById(
        @PathVariable Long id,
        @RequestHeader("farmid") Long farmId
    ) {
        ViewCattleTransferDto dto = cattleTransferService.getTransferById(id, farmId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "", false, dto));
    }
}
