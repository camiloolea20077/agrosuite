package com.erp.backend_erp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.terceros.AutoCompleteDto;
import com.erp.backend_erp.dto.terceros.AutoCompleteModelDto;
import com.erp.backend_erp.dto.terceros.CreateTerceroDto;
import com.erp.backend_erp.dto.terceros.TerceroDto;
import com.erp.backend_erp.dto.terceros.UpdateTerceroDto;
import com.erp.backend_erp.services.TerceroService;
import com.erp.backend_erp.util.ApiResponse;

@RestController
@RequestMapping("/terceros")
public class TercerosController {
    
    @Autowired
    private TerceroService tercerosService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createEmployees(
		@RequestHeader("farmid") Long farmId,
        @Valid @RequestBody CreateTerceroDto createTerceroDto) throws Exception {
        try {
			createTerceroDto.setFarmId(farmId);
            TerceroDto savedUser = tercerosService
                .createTercero(createTerceroDto);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                "Registro creado exitosamente", false, savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Object>> update(
        @RequestHeader("farmid") Long farmId,
        @Valid @RequestBody UpdateTerceroDto updateTerceroDto) throws Exception {
        try {
            Boolean isUpdated = tercerosService.updateTercero(updateTerceroDto);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
                    "Registro actualizado correctamente", false, isUpdated);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getTerceroById(@PathVariable Long id,
        @RequestHeader("farmid") Long farmId) throws Exception {
        try {
            TerceroDto terceroDto = tercerosService.getTerceroById(id, farmId);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
                    "Registro encontrado exitosamente", false, terceroDto);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @PostMapping("/autocomplete")
    public ResponseEntity<ApiResponse<Object>> autocompleteTerceros(
            @RequestHeader("farmid") Long farmId,
            @Valid @RequestBody AutoCompleteDto<Object> autoCompleteDto) {

        try {
            List<AutoCompleteModelDto> autoCompleteModelDto = this.tercerosService
                    .autoCompleteTerceros(autoCompleteDto, farmId);

            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "Autocomplete Encontrado", false, autoCompleteModelDto);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

}
