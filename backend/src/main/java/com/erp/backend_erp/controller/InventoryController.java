package com.erp.backend_erp.controller;

import java.util.List;

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
import com.erp.backend_erp.dto.inventory.ListInventoryDto;
import com.erp.backend_erp.dto.inventory.ReleaseReservedStockDto;
import com.erp.backend_erp.dto.inventory.ReserveStockDto;
import com.erp.backend_erp.dto.inventory.UpdateInventoryDto;
import com.erp.backend_erp.dto.inventory.UpdateStockDto;
import com.erp.backend_erp.services.InventoryService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // Crear
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createInventory(
            @RequestHeader("farmid") Long farmId,
            @RequestHeader("user") Long userId,
            @Valid @RequestBody CreateInventoryDto createInventoryDto) throws Exception {
        try {
            createInventoryDto.setFarmId(farmId);
            createInventoryDto.setCreatedBy(userId);
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

    // Actualizar
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Object>> updateInventory(
            @RequestHeader(value = "userid", required = false) Long userId,
            @Valid @RequestBody UpdateInventoryDto updateInventoryDto) throws Exception {
        try {
            if (userId != null) {
                updateInventoryDto.setUpdatedBy(userId);
            }
            Boolean isUpdated = inventoryService.update(updateInventoryDto);
            ApiResponse<Object> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Registro actualizado correctamente",
                    false,
                    isUpdated);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    // Paginación
    @PostMapping("/page")
    public ResponseEntity<ApiResponse<Object>> paginateInventory(
            @RequestHeader("farmid") Long farmId,
            @RequestBody PageableDto<Object> pageableDto) {
        pageableDto.setFarmId(farmId);
        PageImpl<InventoryTableDto> page = inventoryService.getPage(pageableDto);
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Datos obtenidos correctamente",
                false,
                page);
        return ResponseEntity.ok(response);
    }

    // Buscar por ID (scoped por farm)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryDto>> findById(
            @PathVariable Long id,
            @RequestHeader("farmid") Long farmId) {
        InventoryDto inventory = inventoryService.findById(id, farmId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Elemento encontrado", false, inventory));
    }

    // Eliminar
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

    // ---- ENDPOINTS DE STOCK ----

    // Actualizar stock directo
    @PostMapping("/update-stock")
    public ResponseEntity<ApiResponse<Object>> updateStock(
            @RequestHeader("user") Long userId,
            @Valid @RequestBody UpdateStockDto dto) {
        Boolean ok = inventoryService.updateStock(dto.getInventoryId(), dto.getNewQuantity(), userId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Stock actualizado correctamente", false, ok));
    }

    // Reservar stock
    @PostMapping("/reserve-stock")
    public ResponseEntity<ApiResponse<Object>> reserveStock(
            @RequestHeader("user") Long userId,
            @Valid @RequestBody ReserveStockDto dto) {
        Boolean ok = inventoryService.reserveStock(dto.getInventoryId(), dto.getQuantity(), userId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Stock reservado correctamente", false, ok));
    }

    // Liberar stock reservado
    @PostMapping("/release-reserved-stock")
    public ResponseEntity<ApiResponse<Object>> releaseReservedStock(
            @RequestHeader("user") Long userId,
            @Valid @RequestBody ReleaseReservedStockDto dto) {
        Boolean ok = inventoryService.releaseReservedStock(dto.getInventoryId(), dto.getQuantity(), userId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Reserva liberada correctamente", false, ok));
    }
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Object>> findAllActive(
        @RequestHeader("farmId") Long farmId) {
        List<ListInventoryDto> list = inventoryService.getInventory(farmId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Datos obtenidos correctamente", false, list)
        );
    }
}