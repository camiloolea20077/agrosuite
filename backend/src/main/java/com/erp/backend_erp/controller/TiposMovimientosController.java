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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.inventory.CreateTiposMovimientosDto;
import com.erp.backend_erp.dto.inventory.TiposMovimientosDto;
import com.erp.backend_erp.dto.inventory.UpdateTiposMovimientosDto;
import com.erp.backend_erp.services.TiposMovimientosService;
import com.erp.backend_erp.util.ApiResponse;

@RestController
@RequestMapping("/tipos-movimientos")
public class TiposMovimientosController {

    @Autowired
    private TiposMovimientosService tiposMovimientosService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> create(
            @RequestHeader(value = "user", required = false) Long userId,
            @Valid @RequestBody CreateTiposMovimientosDto dto) {
        TiposMovimientosDto saved = tiposMovimientosService.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(),
                "Registro creado exitosamente", false, saved));
    }

    @PostMapping("/create/bulk")
    public ResponseEntity<ApiResponse<Object>> createBulk(
            @Valid @RequestBody List<CreateTiposMovimientosDto> list) {
        List<TiposMovimientosDto> saved = tiposMovimientosService.createAll(list);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(),
                "Registros creados", false, saved));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Object>> update(
            @RequestHeader(value = "user", required = false) Long userId,
            @Valid @RequestBody UpdateTiposMovimientosDto dto) {
        Boolean ok = tiposMovimientosService.update(dto);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Registro actualizado correctamente", false, ok));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Object>> activate(@PathVariable Long id) {
        Boolean ok = tiposMovimientosService.activate(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Registro activado correctamente", false, ok));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TiposMovimientosDto>> findById(@PathVariable Long id) {
        TiposMovimientosDto dto = tiposMovimientosService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Elemento encontrado", false, dto));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Object>> findAllActive() {
        List<TiposMovimientosDto> list = tiposMovimientosService.findAllActive();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Datos obtenidos correctamente", false, list));
    }

    // @PostMapping("/page")
    // public ResponseEntity<ApiResponse<Object>> page(@RequestBody PageableDto<Object> pageableDto) {
    //     PageImpl<TiposMovimientosTableDto> page = tiposMovimientosService.getPage(pageableDto);
    //     return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
    //             "Datos obtenidos correctamente", false, page));
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        Boolean ok = tiposMovimientosService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),
                "Registro eliminado correctamente", false, ok));
    }
}