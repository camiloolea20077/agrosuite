package com.erp.backend_erp.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.births.DesteteTableDto;
import com.erp.backend_erp.dto.births.MigrarTerneroDto;
import com.erp.backend_erp.dto.births.ResultadoMigracionDto;
import com.erp.backend_erp.services.DesteteService;
import com.erp.backend_erp.util.ApiResponse;

@RestController
@RequestMapping("/destetes")
public class DesteteController {

    @Autowired
    private DesteteService desteteService;

    @GetMapping("/terneros-listos")
    public ResponseEntity<ApiResponse<List<DesteteTableDto>>> obtenerTernerosParaDestetar(
            @RequestHeader("farmid") Long farmId) {
        try {
            List<DesteteTableDto> terneros = desteteService.obtenerTernerosParaDestetar(farmId);
            
            String mensaje = terneros.isEmpty() 
                ? "No hay terneros listos para destetar" 
                : "Encontrados " + terneros.size() + " terneros listos para destetar";
                
            ApiResponse<List<DesteteTableDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(), 
                mensaje, 
                false, 
                terneros
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener terneros para destetar", ex);
        }
    }
    @PostMapping("/migrar")
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

    @GetMapping("/historial")
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

    @PostMapping("/migrar-lote")
    public ResponseEntity<ApiResponse<List<ResultadoMigracionDto>>> migrarLoteTerneros(
            @RequestHeader("farmid") Long farmId,
            @Valid @RequestBody List<MigrarTerneroDto> migrarDtos) {
        try {
            List<ResultadoMigracionDto> resultados = migrarDtos.stream()
                .map(dto -> desteteService.migrarTernero(dto, farmId))
                .toList();
            
            long exitosos = resultados.stream().mapToLong(r -> r.getSuccess() ? 1L : 0L).sum();
            
            ApiResponse<List<ResultadoMigracionDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                String.format("Procesados %d terneros. %d exitosos, %d fallidos", 
                    resultados.size(), exitosos, resultados.size() - exitosos),
                false,
                resultados
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw new RuntimeException("Error al migrar lote de terneros", ex);
        }
    }
}