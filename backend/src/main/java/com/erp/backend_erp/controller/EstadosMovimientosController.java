package com.erp.backend_erp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.inventory.CreateEstadosMovimientosDto;
import com.erp.backend_erp.dto.inventory.EstadosMovimientosDto;
import com.erp.backend_erp.dto.inventory.UpdateEstadosMovimientosDto;
import com.erp.backend_erp.services.EstadosMovimientosService;
import com.erp.backend_erp.util.ApiResponse;

@RestController
@RequestMapping("/estados-movimientos")
public class EstadosMovimientosController {

    @Autowired
    private EstadosMovimientosService estadosMovimientosService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> create(@Valid @RequestBody CreateEstadosMovimientosDto dto) {
        EstadosMovimientosDto saved = estadosMovimientosService.create(dto);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.CREATED.value(), "Registro creado exitosamente", false, saved)
        );
    }

    @PostMapping("/create/bulk")
    public ResponseEntity<ApiResponse<Object>> createBulk(
            @Valid @RequestBody List<CreateEstadosMovimientosDto> list) {
        List<EstadosMovimientosDto> saved = estadosMovimientosService.createAll(list);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.CREATED.value(), "Registros creados", false, saved)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Object>> update(@Valid @RequestBody UpdateEstadosMovimientosDto dto) {
        Boolean ok = estadosMovimientosService.update(dto);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro actualizado correctamente", false, ok)
        );
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Object>> activate(@PathVariable Long id) {
        Boolean ok = estadosMovimientosService.activate(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro activado correctamente", false, ok)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EstadosMovimientosDto>> findById(@PathVariable Long id) {
        EstadosMovimientosDto dto = estadosMovimientosService.findById(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Elemento encontrado", false, dto)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Object>> findAllActive() {
        List<EstadosMovimientosDto> list = estadosMovimientosService.findAllActive();
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Datos obtenidos correctamente", false, list)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        Boolean ok = estadosMovimientosService.delete(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro eliminado correctamente", false, ok)
        );
    }
}