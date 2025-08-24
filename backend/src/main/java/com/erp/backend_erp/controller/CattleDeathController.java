package com.erp.backend_erp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.cattleDeath.CattleDeathDto;
import com.erp.backend_erp.dto.cattleDeath.CreateCattleDeathDto;
import com.erp.backend_erp.dto.cattleDeath.DeathsTableDto;
import com.erp.backend_erp.services.CattleDeathService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/cattle-death")
public class CattleDeathController {
        
    @Autowired
    private CattleDeathService cattleDeathService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createCattle(
		@RequestHeader("farmid") Long farmId,
        @RequestHeader("user") Long userId,
        @Valid @RequestBody CreateCattleDeathDto createCattleDto) throws Exception {
        try {
			createCattleDto.setFarmId(farmId);
            createCattleDto.setUsuarioRegistro(userId);
            CattleDeathDto savedUser = cattleDeathService
                .create(createCattleDto);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED.value(),
                "Registro creado exitosamente", false, savedUser);
            return ResponseEntity.ok(response);
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
            Page<DeathsTableDto> convention = this.cattleDeathService.pageDeaths(pageableDto);
            if (convention.isEmpty())
                throw new GlobalException(HttpStatus.PARTIAL_CONTENT, "No se encontraron registros");
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, convention);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    /**
     * Obtener detalle de una muerte
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CattleDeathDto>> findById(
            @PathVariable Integer id,
            @RequestHeader("farmId") Long farmId) {
        CattleDeathDto viewCattleDeathDto = cattleDeathService.findByIds(id, farmId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Elemento encontrado", false, viewCattleDeathDto));
    }
}
