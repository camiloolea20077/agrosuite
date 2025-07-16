package com.erp.backend_erp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.clients.ClientsDto;
import com.erp.backend_erp.dto.clients.ClientsTableDto;
import com.erp.backend_erp.dto.clients.CreateClientsDto;
import com.erp.backend_erp.dto.clients.UpdateClientsDto;
import com.erp.backend_erp.services.ClientsService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/clients")
public class ClientsController {
    
    @Autowired
    ClientsService clientsService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createClients(
        @Valid @RequestBody CreateClientsDto createClientsDto) throws Exception {
        try {
            ClientsDto savedUser = clientsService
                .create(createClientsDto);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                "Registro creado exitosamente", false, savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PutMapping("/update")
	public ResponseEntity<ApiResponse<Object>> update(@Valid @RequestBody UpdateClientsDto updateClientsDto)
			throws Exception {
		try {
			Boolean isUpdated = clientsService.update(updateClientsDto);
			ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
					"Registro actualizado correctamente", false, isUpdated);
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
            Page<ClientsTableDto> convention = this.clientsService.pageClients(pageableDto);
            if (convention.isEmpty())
                throw new GlobalException(HttpStatus.PARTIAL_CONTENT, "No se encontraron registros");
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, convention);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

}
