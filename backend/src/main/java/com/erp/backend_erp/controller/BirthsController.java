package com.erp.backend_erp.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.births.BirthsDto;
import com.erp.backend_erp.dto.births.BirthsTableDto;
import com.erp.backend_erp.dto.births.CreateBirthsDto;
import com.erp.backend_erp.dto.births.DesteteTableDto;
import com.erp.backend_erp.dto.births.MigrarTerneroDto;
import com.erp.backend_erp.dto.births.ResultadoMigracionDto;
import com.erp.backend_erp.dto.births.UpdateBirthsDto;
import com.erp.backend_erp.services.BirthsService;
import com.erp.backend_erp.services.DesteteService;
import com.erp.backend_erp.util.ApiResponse;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@RestController
@RequestMapping("/births")
public class BirthsController {
    
    @Autowired
    BirthsService birthsService;

    @Autowired
    private DesteteService desteteService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createCattle(
		@RequestHeader("farmid") Long farmId,
        @Valid @RequestBody CreateBirthsDto createCattleDto) throws Exception {
        try {
			createCattleDto.setFarmId(farmId);
            BirthsDto savedUser = birthsService
                .create(createCattleDto);
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
            @Valid @RequestBody List<CreateBirthsDto> createCattleList) throws Exception {
        try {
            // Asignar el farmId a cada uno
            for (CreateBirthsDto dto : createCattleList) {
                dto.setFarmId(farmId);
            }

            // Guardar todos los registros
            List<BirthsDto> savedCattle = birthsService.createAll(createCattleList);

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

    @PutMapping("/update")
	public ResponseEntity<ApiResponse<Object>> update(
		@RequestHeader("farmid") Long farmId,
		@Valid @RequestBody UpdateBirthsDto updateBirthsDto)
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
    public ResponseEntity<ApiResponse<Object>> delete(
            @PathVariable("id") Long id,
            @RequestHeader("farmid") Long farmId) throws Exception {
        try {
            Boolean isDeleted = birthsService.delete(id);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(),
                    "Registro eliminado correctamente", false, isDeleted);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BirthsDto>> findById(
            @PathVariable Long id,
            @RequestHeader("farmId") Long farmId) {
        BirthsDto inventory = birthsService.findById(id, farmId);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Elemento encontrado", false, inventory));
    }

    @PostMapping("/page")
    public ResponseEntity<ApiResponse<Object>> listConventions(
			@RequestHeader("farmid") Long farmId,
            @Valid @RequestBody PageableDto<Object> pageableDto) {
        try {
			pageableDto.setFarmId(farmId);
            Page<BirthsTableDto> convention = this.birthsService.pageGanado(pageableDto);
            if (convention.isEmpty())
                throw new GlobalException(HttpStatus.PARTIAL_CONTENT, "No se encontraron registros");
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, convention);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @GetMapping("/proximos-destetes")
    public ResponseEntity<ApiResponse<List<DesteteTableDto>>> obtenerProximosDestetes(
            @RequestHeader("farmid") Long farmId,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        try {
            List<DesteteTableDto> terneros = desteteService.obtenerProximosDestetes(farmId, limit);
            
            String mensaje = terneros.isEmpty() 
                ? "No hay próximos destetes programados" 
                : "Encontrados " + terneros.size() + " próximos destetes";
                
            ApiResponse<List<DesteteTableDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(), 
                mensaje, 
                false, 
                terneros
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener próximos destetes", ex);
        }
    }

    @PostMapping("/migrar-ternero")
    public ResponseEntity<ApiResponse<ResultadoMigracionDto>> migrarTernero(
            @RequestHeader("farmid") Long farmId,
            @Valid @RequestBody MigrarTerneroDto migrarDto) {
        try {
            ResultadoMigracionDto resultado = desteteService.migrarTernero(migrarDto, farmId);
            
            HttpStatus status = resultado.getSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            
            ApiResponse<ResultadoMigracionDto> response = new ApiResponse<>(
                status.value(),
                resultado.getMessage(),
                !resultado.getSuccess(),
                resultado
            );
            
            return ResponseEntity.status(status).body(response);
        } catch (Exception ex) {
            throw new RuntimeException("Error al migrar ternero", ex);
        }
    }

    @GetMapping("/historial-destetes")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> obtenerHistorialDestetes(
            @RequestHeader("farmid") Long farmId) {
        try {
            List<Map<String, Object>> historial = desteteService.obtenerHistorialDestetes(farmId);
            
            ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Historial de destetes obtenido exitosamente",
                false,
                historial
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener historial de destetes", ex);
        }
    }
}
