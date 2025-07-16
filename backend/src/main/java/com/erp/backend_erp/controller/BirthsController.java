package com.erp.backend_erp.controller;

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

import com.erp.backend_erp.dto.births.BirthsDto;
import com.erp.backend_erp.dto.births.BirthsTableDto;
import com.erp.backend_erp.dto.births.CreateBirthsDto;
import com.erp.backend_erp.dto.births.UpdateBirthsDto;
import com.erp.backend_erp.services.BirthsService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/births")
public class BirthsController {
    
    @Autowired
    BirthsService birthsService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createCattle(
        @Valid @RequestBody CreateBirthsDto createCattleDto) throws Exception {
        try {
            BirthsDto savedUser = birthsService
                .create(createCattleDto);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                "Registro creado exitosamente", false, savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PutMapping("/update")
	public ResponseEntity<ApiResponse<Object>> update(@Valid @RequestBody UpdateBirthsDto updateBirthsDto)
			throws Exception {
		try {
			Boolean isUpdated = birthsService.update(updateBirthsDto);
			ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
					"Registro actualizado correctamente", false, isUpdated);
			return ResponseEntity.ok(response);
		}
		catch (Exception ex) {
			throw ex;
		}
	}

    @DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable("id") Long id) throws Exception {
		try {
			Boolean isDeleted = birthsService.delete(id);
			ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "Registro eliminado correctamente",
					false, isDeleted);
			return ResponseEntity.ok(response);
		}
		catch (Exception ex) {
			throw ex;
		}
	}
    @GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> findById(@PathVariable("id") Long id) {
		try {
			BirthsDto object = this.birthsService.findById(id);
			ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "Registro Encontrado", false, object);
			return ResponseEntity.ok(response);
		}
		catch (Exception ex) {
			throw ex;
		}
	}
    @PostMapping("/page")
    public ResponseEntity<ApiResponse<Object>> listConventions(
            @Valid @RequestBody PageableDto<Object> pageableDto) {
        try {
            Page<BirthsTableDto> convention = this.birthsService.pageGanado(pageableDto);
            if (convention.isEmpty())
                throw new GlobalException(HttpStatus.PARTIAL_CONTENT, "No se encontraron registros");
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, convention);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
