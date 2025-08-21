package com.erp.backend_erp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.erp.backend_erp.dto.inventory.CreateEstadosInventarioDto;
import com.erp.backend_erp.dto.inventory.EstadosInventarioDto;
import com.erp.backend_erp.dto.inventory.EstadosInventarioTableDto;
import com.erp.backend_erp.dto.inventory.UpdateEstadosInventarioDto;
import com.erp.backend_erp.services.EstadosInventarioService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/estados-inventario")
public class EstadosInventarioController {

    @Autowired
    private EstadosInventarioService estadosInventarioService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> create(@Valid @RequestBody CreateEstadosInventarioDto dto) {
        EstadosInventarioDto saved = estadosInventarioService.create(dto);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.CREATED.value(), "Registro creado exitosamente", false, saved)
        );
    }

    @PostMapping("/create/bulk")
    public ResponseEntity<ApiResponse<Object>> createBulk(@Valid @RequestBody List<CreateEstadosInventarioDto> list) {
        List<EstadosInventarioDto> saved = estadosInventarioService.createAll(list);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.CREATED.value(), "Registros creados", false, saved)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Object>> update(@Valid @RequestBody UpdateEstadosInventarioDto dto) {
        Boolean ok = estadosInventarioService.update(dto);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro actualizado correctamente", false, ok)
        );
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Object>> activate(@PathVariable Long id) {
        Boolean ok = estadosInventarioService.activate(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro activado correctamente", false, ok)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EstadosInventarioDto>> findById(@PathVariable Long id) {
        EstadosInventarioDto dto = estadosInventarioService.findById(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Elemento encontrado", false, dto)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Object>> findAllActive() {
        List<EstadosInventarioDto> list = estadosInventarioService.findAllActive();
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Datos obtenidos correctamente", false, list)
        );
    }
    @PostMapping("/page")
    public ResponseEntity<ApiResponse<Object>> listStateInventory(
            @Valid @RequestBody PageableDto<Object> pageableDto) {
        try {
            Page<EstadosInventarioTableDto> convention = this.estadosInventarioService.page(pageableDto);
            if (convention.isEmpty())
                throw new GlobalException(HttpStatus.PARTIAL_CONTENT, "No se encontraron registros");
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, convention);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        Boolean ok = estadosInventarioService.delete(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro eliminado correctamente", false, ok)
        );
    }
}