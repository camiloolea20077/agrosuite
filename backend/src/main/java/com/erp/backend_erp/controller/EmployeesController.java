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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.employees.CreateEmployeesDto;
import com.erp.backend_erp.dto.employees.EmployeesDto;
import com.erp.backend_erp.dto.employees.EmployeesTableDto;
import com.erp.backend_erp.dto.employees.UpdateEmployeesDto;
import com.erp.backend_erp.services.EmployeesService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/employees")
public class EmployeesController {
    

    @Autowired
    EmployeesService employeesService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createEmployees(
		@RequestHeader("farmid") Long farmId,
        @Valid @RequestBody CreateEmployeesDto createEmployeesDto) throws Exception {
        try {
			createEmployeesDto.setFarmId(farmId);
            EmployeesDto savedUser = employeesService
                .create(createEmployeesDto);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                "Registro creado exitosamente", false, savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @PutMapping("/update")
	public ResponseEntity<ApiResponse<Object>> update(@Valid @RequestBody UpdateEmployeesDto updateEmployeesDto)
			throws Exception {
		try {
			Boolean isUpdated = employeesService.update(updateEmployeesDto);
			ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
					"Registro actualizado correctamente", false, isUpdated);
			return ResponseEntity.ok(response);
		}
		catch (Exception ex) {
			throw ex;
		}
	}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(
            @PathVariable("id") Long id,
            @RequestHeader("farmid") Long farmId) throws Exception {
        try {
            Boolean isDeleted = employeesService.delete(id);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
                    "Registro eliminado correctamente", false, isDeleted);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> findById(@PathVariable("id") Long id,
		@RequestHeader("farmId") Long farmId) {
		try {
			EmployeesDto object = this.employeesService.findById(id, farmId);
			ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "Registro Encontrado", false, object);
			return ResponseEntity.ok(response);
		}
		catch (Exception ex) {
			throw ex;
		}
	}
    @PostMapping("/page")
    public ResponseEntity<ApiResponse<Object>> pageEmployees(
		@RequestHeader("farmid") Long farmId,
            @Valid @RequestBody PageableDto<Object> pageableDto) {
        try {
			pageableDto.setFarmId(farmId);
            Page<EmployeesTableDto> employees = this.employeesService.pageEmployees(pageableDto);
            if (employees.isEmpty())
                throw new GlobalException(HttpStatus.PARTIAL_CONTENT, "No se encontraron registros");
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, employees);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Object>> findAll(@RequestHeader("farmid") Long farmId) {
        try {
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, employeesService.getEmployees(farmId));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
