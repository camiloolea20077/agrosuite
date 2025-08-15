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

import com.erp.backend_erp.dto.inventory.CreateTiposInsumosDto;
import com.erp.backend_erp.dto.inventory.TiposInsumosDto;
import com.erp.backend_erp.dto.inventory.UpdateTiposInsumosDto;
import com.erp.backend_erp.services.TiposInsumosService;
import com.erp.backend_erp.util.ApiResponse;

@RestController
@RequestMapping("/tipos-insumos")
public class TiposInsumosController {

    @Autowired
    private TiposInsumosService tiposInsumosService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> create(@Valid @RequestBody CreateTiposInsumosDto dto) {
        TiposInsumosDto saved = tiposInsumosService.create(dto);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.CREATED.value(), "Registro creado exitosamente", false, saved)
        );
    }

    @PostMapping("/create/bulk")
    public ResponseEntity<ApiResponse<Object>> createBulk(@Valid @RequestBody List<CreateTiposInsumosDto> list) {
        List<TiposInsumosDto> saved = tiposInsumosService.createAll(list);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.CREATED.value(), "Registros creados", false, saved)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Object>> update(@Valid @RequestBody UpdateTiposInsumosDto dto) {
        Boolean ok = tiposInsumosService.update(dto);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro actualizado correctamente", false, ok)
        );
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Object>> activate(@PathVariable Long id) {
        Boolean ok = tiposInsumosService.activate(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro activado correctamente", false, ok)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TiposInsumosDto>> findById(@PathVariable Long id) {
        TiposInsumosDto dto = tiposInsumosService.findById(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Elemento encontrado", false, dto)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Object>> findAllActive() {
        List<TiposInsumosDto> list = tiposInsumosService.findAllActive();
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Datos obtenidos correctamente", false, list)
        );
    }

    // @PostMapping("/page")
    // public ResponseEntity<ApiResponse<Object>> page(@RequestBody PageableDto<Object> pageableDto) {
    //     PageImpl<TiposInsumosTableDto> page = tiposInsumosService.getPage(pageableDto);
    //     return ResponseEntity.ok(
    //         new ApiResponse<>(HttpStatus.OK.value(), "Datos obtenidos correctamente", false, page)
    //     );
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        Boolean ok = tiposInsumosService.delete(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro eliminado correctamente", false, ok)
        );
    }
}