package com.erp.backend_erp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.companies.CompaniesDto;
import com.erp.backend_erp.dto.companies.CreateCompaniesDto;
import com.erp.backend_erp.dto.companies.UpdateCompaniesDto;
import com.erp.backend_erp.services.CompanieService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/companies")
public class CompaniesController {
    
    @Autowired
    CompanieService companiesService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createCompanies(
        @Valid @RequestBody CreateCompaniesDto createCompaniesDto) throws Exception {
        try {
            CompaniesDto savedUser = companiesService
                .create(createCompaniesDto);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                "Registro creado exitosamente", false, savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PutMapping("/update")
	public ResponseEntity<ApiResponse<Object>> update(@Valid @RequestBody UpdateCompaniesDto updateCompaniesDto)
			throws Exception {
		try {
			Boolean isUpdated = companiesService.update(updateCompaniesDto);
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
			Boolean isDeleted = companiesService.delete(id);
			ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "Registro eliminado correctamente",
					false, isDeleted);
			return ResponseEntity.ok(response);
		}
		catch (Exception ex) {
			throw ex;
		}
	}
}
