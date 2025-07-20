package com.erp.backend_erp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.inventory.CreateInventoryDto;
import com.erp.backend_erp.dto.inventory.InventoryDto;
import com.erp.backend_erp.dto.inventory.InventoryTableDto;
import com.erp.backend_erp.dto.inventory.UpdateInventoryDto;
import com.erp.backend_erp.services.InventoryService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	@Autowired
	InventoryService inventoryService;

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Object>> createInventory(
			@RequestHeader("farmid") Long farmId,
			@Valid @RequestBody CreateInventoryDto createInventoryDto) throws Exception {
		try {
			createInventoryDto.setFarmId(farmId);
			InventoryDto savedInventory = inventoryService.create(createInventoryDto);
			ApiResponse<Object> response = new ApiResponse<>(
					HttpStatus.CREATED.value(),
					"Registro creado exitosamente",
					false,
					savedInventory);
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			throw ex; 
		}
	}

	@PutMapping("/update")
	public ResponseEntity<ApiResponse<Object>> updateInventory(
			@Valid @RequestBody UpdateInventoryDto updateInventoryDto)
			throws Exception {
		try {
			Boolean isUpdated = inventoryService.update(updateInventoryDto);
			ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
					"Registro actualizado correctamente", false, isUpdated);
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			throw ex;
		}
	}

	@PostMapping("/page")
	public ResponseEntity<ApiResponse<Object>> paginateInventory(
		@RequestHeader("farmid") Long farmId,
		@RequestBody PageableDto<Object> pageableDto) {
		pageableDto.setFarmId(farmId);
		PageImpl<InventoryTableDto> page = inventoryService.pageInventory(pageableDto);
		ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
				"Datos obtenidos correctamente", false, page);
		return ResponseEntity.ok(response);
	}

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryDto>> findById(
            @PathVariable Long id,
            @RequestHeader("farmId") Long farmId) {
        InventoryDto inventory = inventoryService.findById(id, farmId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Elemento encontrado", false, inventory));
    }
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(
			@PathVariable("id") Long id,
			@RequestHeader("farmid") Long farmId) throws Exception {
		try {
			Boolean isDeleted = inventoryService.delete(id);
			ApiResponse<Object> response = new ApiResponse<>(
					HttpStatus.OK.value(),
					"Registro eliminado correctamente",
					false,
					isDeleted);
			return ResponseEntity.ok(response);
		} catch (Exception ex) {
			throw ex;
		}
	}


}
