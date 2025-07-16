package com.erp.backend_erp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.roles.CreateRoleDto;
import com.erp.backend_erp.dto.roles.RoleDto;
import com.erp.backend_erp.dto.roles.UpdateRoleDto;
import com.erp.backend_erp.services.RolesService;
import com.erp.backend_erp.util.ApiResponse;

@RestController
@RequestMapping("/roles")
public class RolesController {
    @Autowired
    RolesService rolesService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createCompanies(
        @Valid @RequestBody CreateRoleDto createRolesDto) throws Exception {
        try {
            RoleDto savedRoles = rolesService
                .create(createRolesDto);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                "Registro creado exitosamente", false, savedRoles);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable("id") Long id) throws Exception {
		try {
			Boolean isDeleted = rolesService.delete(id);
			ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "Registro eliminado correctamente",
					false, isDeleted);
			return ResponseEntity.ok(response);
		}
		catch (Exception ex) {
			throw ex;
		}
	}

    @PutMapping("/update")
	public ResponseEntity<ApiResponse<Object>> update(@Valid @RequestBody UpdateRoleDto updateRoleDto)
			throws Exception {
		try {
			Boolean isUpdated = rolesService.update(updateRoleDto);
			ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
					"Registro actualizado correctamente", false, isUpdated);
			return ResponseEntity.ok(response);
		}
		catch (Exception ex) {
			throw ex;
		}
	}

}
