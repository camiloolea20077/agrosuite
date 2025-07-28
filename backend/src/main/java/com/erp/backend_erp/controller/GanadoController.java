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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.ganado.CreateGanadoDto;
import com.erp.backend_erp.dto.ganado.GanadoDto;
import com.erp.backend_erp.dto.ganado.GanadoTableDto;
import com.erp.backend_erp.dto.ganado.UpdateGanadoDto;
import com.erp.backend_erp.services.GanadoService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/cattle")
public class GanadoController {


    @Autowired
    GanadoService ganadoService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createCattle(
            @RequestHeader("farmid") Long farmId,
            @Valid @RequestBody CreateGanadoDto createCattleDto) throws Exception {
        try {
            createCattleDto.setFarmId(farmId);
            GanadoDto savedUser = ganadoService.create(createCattleDto);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                    "Registro creado exitosamente", false, savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
@PostMapping("/create/batch")
public ResponseEntity<ApiResponse<Object>> createMultipleCattle(
        @RequestHeader("farmid") Long farmId,
        @Valid @RequestBody List<CreateGanadoDto> createCattleList) throws Exception {
    try {
        // Asignar el farmId a cada uno
        for (CreateGanadoDto dto : createCattleList) {
            dto.setFarmId(farmId);
        }

        // Guardar todos los registros
        List<GanadoDto> savedCattle = ganadoService.createAll(createCattleList);

        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Registros creados exitosamente",
                false,
                savedCattle
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception ex) {
        throw ex;
    }
}

    @PostMapping("/page")
    public ResponseEntity<ApiResponse<Object>> listConventions(
            @RequestHeader("farmid") Long farmId,
            @Valid @RequestBody PageableDto<Object> pageableDto) {
        try {
            pageableDto.setFarmId(farmId);
            Page<GanadoTableDto> convention = ganadoService.pageGanado(pageableDto);
            if (convention.isEmpty())
                throw new GlobalException(HttpStatus.PARTIAL_CONTENT, "No se encontraron registros");
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, convention);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Object>> update(
            @RequestHeader("farmid") Long farmId,
            @Valid @RequestBody UpdateGanadoDto updateGanadoDto) throws Exception {
        try {
            Boolean isUpdated = ganadoService.update(updateGanadoDto);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
                    "Registro actualizado correctamente", false, isUpdated);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(
            @PathVariable("id") Long id,
            @RequestHeader("farmid") Long farmId) throws Exception {
        try {
            Boolean isDeleted = ganadoService.delete(id);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
                    "Registro eliminado correctamente", false, isDeleted);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GanadoDto>> findById(
            @PathVariable Long id,
            @RequestHeader("farmId") Long farmId) {
        GanadoDto inventory = ganadoService.findById(id, farmId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Elemento encontrado", false, inventory));
    }
}
