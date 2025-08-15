package com.erp.backend_erp.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.InventoryMovements.CreateInventoryMovementsDto;
import com.erp.backend_erp.dto.InventoryMovements.InventoryMovementsDto;
import com.erp.backend_erp.dto.InventoryMovements.InventoryMovementsTableDto;
import com.erp.backend_erp.dto.InventoryMovements.UpdateInventoryMovementsDto;
import com.erp.backend_erp.dto.inventory.TiposMovimientosDto;
import com.erp.backend_erp.services.InventoryMovementsService;
import com.erp.backend_erp.services.TiposMovimientosService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/inventory-movements")
public class InventoryMovementsController {

    @Autowired
    private InventoryMovementsService inventoryMovementsService;

    @Autowired
    private TiposMovimientosService tiposMovimientosService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> create(
                @RequestHeader("farmid") Long farmId,
                @RequestHeader("user") Long userId,
                @Valid @RequestBody CreateInventoryMovementsDto dto) {
            try {
                dto.setFarmId(farmId);
                dto.setCreatedBy(userId);
                
                // Obtener el tipo de movimiento para determinar si es entrada o salida
                TiposMovimientosDto tipoMovimiento = tiposMovimientosService.findById(dto.getTipoMovimientoId());
                
                InventoryMovementsDto saved;
                
                if (tipoMovimiento.getEsEntrada() == 1L) {
                    // Es una entrada - aumenta el stock
                    saved = inventoryMovementsService.processEntry(dto);
                } else if (tipoMovimiento.getEsSalida() == 1L) {
                    // Es una salida - disminuye el stock
                    saved = inventoryMovementsService.processExit(dto);
                } else {
                    saved = inventoryMovementsService.create(dto);
                }
                return ResponseEntity.ok(
                    new ApiResponse<>(HttpStatus.CREATED.value(), "Movimiento creado exitosamente", false, saved)
                );
            } catch (GlobalException e) {
                throw e;
            } catch (Exception e) {
                throw new GlobalException(HttpStatus.BAD_REQUEST, "Error al crear el movimiento: " + e.getMessage());
            }
        }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Object>> update(
            @RequestHeader("user") Long userId,
            @RequestHeader("farmid") Long farmId,
            @Valid @RequestBody UpdateInventoryMovementsDto dto) {
        dto.setUpdatedBy(userId);
        dto.setFarmId(farmId);
        Boolean ok = inventoryMovementsService.update(dto);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Movimiento actualizado correctamente", false, ok)
        );
    }

    @GetMapping("/by-inventory/{inventoryId}")
    public ResponseEntity<ApiResponse<Object>> listByInventory(
            @PathVariable Long inventoryId,
            @RequestHeader("farmid") Long farmId) {
        List<InventoryMovementsDto> list =
            inventoryMovementsService.findByInventoryAndFarm(inventoryId, farmId);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Datos obtenidos correctamente", false, list)
        );
    }

    @GetMapping("/by-dates")
    public ResponseEntity<ApiResponse<Object>> listByDateRange(
            @RequestHeader("farmid") Long farmId,
            @RequestParam("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<InventoryMovementsDto> list =
            inventoryMovementsService.findByFarmAndDateRange(farmId, start, end);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Datos obtenidos correctamente", false, list)
        );
    }

    @GetMapping("/pending-approvals")
    public ResponseEntity<ApiResponse<Object>> pendingApprovals(
            @RequestHeader("farmid") Long farmId) {

        List<InventoryMovementsDto> list =
            inventoryMovementsService.listPendingApprovals(farmId);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Datos obtenidos correctamente", false, list)
        );
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<Object>> approve(
            @PathVariable Long id,
            @RequestHeader("user") Long approverUserId) {

        Boolean ok = inventoryMovementsService.approve(id, approverUserId);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Movimiento aprobado", false, ok)
        );
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Object>> reject(
            @PathVariable Long id,
            @RequestHeader("user") Long approverUserId,
            @RequestParam(value = "reason", required = false) String reason) {

        Boolean ok = inventoryMovementsService.reject(id, approverUserId, reason);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Movimiento rechazado", false, ok)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryMovementsDto>> findById(
            @PathVariable Long id,
            @RequestHeader("farmid") Long farmId) {

        InventoryMovementsDto dto = inventoryMovementsService.findById(id, farmId);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Elemento encontrado", false, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(
            @PathVariable Long id,
            @RequestHeader("farmid") Long farmId) {

        Boolean ok = inventoryMovementsService.delete(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro eliminado correctamente", false, ok)
        );
    }
@PostMapping("/page")
public ResponseEntity<ApiResponse<Object>> paginateMovements(
        @RequestHeader("farmid") Long farmId,
        @RequestBody PageableDto<Object> pageableDto) {
    pageableDto.setFarmId(farmId);
    PageImpl<InventoryMovementsTableDto> page = inventoryMovementsService.getPage(pageableDto);
    
    ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            "Datos obtenidos correctamente",
            false,
            page);
    return ResponseEntity.ok(response);
}
}