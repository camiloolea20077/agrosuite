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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.suppliers.CreateSuppliersDto;
import com.erp.backend_erp.dto.suppliers.SuppliersDto;
import com.erp.backend_erp.dto.suppliers.SuppliersTableDto;
import com.erp.backend_erp.dto.suppliers.UpdateSuppliersDto;
import com.erp.backend_erp.services.SuppliersService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.PageableDto;
@RestController
@RequestMapping("/suppliers")
public class SuppliersController {

    @Autowired
    private SuppliersService suppliersService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> create(@Valid @RequestBody CreateSuppliersDto dto) {
        SuppliersDto saved = suppliersService.create(dto);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.CREATED.value(), "Registro creado exitosamente", false, saved)
        );
    }

    @PostMapping("/create/bulk")
    public ResponseEntity<ApiResponse<Object>> createBulk(@Valid @RequestBody List<CreateSuppliersDto> list) {
        List<SuppliersDto> saved = suppliersService.createAll(list);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.CREATED.value(), "Registros creados", false, saved)
        );
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Object>> update(@Valid @RequestBody UpdateSuppliersDto dto) {
        Boolean ok = suppliersService.update(dto);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro actualizado correctamente", false, ok)
        );
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Object>> activate(@PathVariable Long id) {
        Boolean ok = suppliersService.activate(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro activado correctamente", false, ok)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SuppliersDto>> findById(@PathVariable Long id) {
        SuppliersDto dto = suppliersService.findById(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Elemento encontrado", false, dto)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Object>> findAllActive() {
        List<SuppliersDto> list = suppliersService.findAllActive();
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Datos obtenidos correctamente", false, list)
        );
    }

    @PostMapping("/page")
    public ResponseEntity<ApiResponse<Object>> page(@RequestBody PageableDto<Object> pageableDto) {
        PageImpl<SuppliersTableDto> page = suppliersService.getPage(pageableDto);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Datos obtenidos correctamente", false, page)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Long id) {
        Boolean ok = suppliersService.delete(id);
        return ResponseEntity.ok(
            new ApiResponse<>(HttpStatus.OK.value(), "Registro eliminado correctamente", false, ok)
        );
    }
}