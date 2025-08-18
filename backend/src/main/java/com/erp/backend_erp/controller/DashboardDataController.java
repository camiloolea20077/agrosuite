package com.erp.backend_erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.births.DashboardData;
import com.erp.backend_erp.dto.births.DesteteTableDto;
import com.erp.backend_erp.services.BirthsService;
import com.erp.backend_erp.services.DashboardService;
import com.erp.backend_erp.util.ApiResponse;

@RestController
@RequestMapping("/dashboard")
public class DashboardDataController {

    @Autowired
    BirthsService birthsService;

    @Autowired
    DashboardService dashboardService;

    @GetMapping("/data")
    public ResponseEntity<ApiResponse<Object>> findListForId(@RequestHeader("farmId") Long farmId) {
        try {
            DashboardData dashboardData = this.birthsService.getDashboardData(farmId);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, dashboardData);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @GetMapping("/destete")
    public ResponseEntity<ApiResponse<Object>> getDesteteList(@RequestHeader("farmId") Long farmId) {
        try {
            List<DesteteTableDto> desteteList = this.dashboardService.listDesteteDashboard(farmId);
            ApiResponse<Object> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Lista de destete cargada correctamente",
                    false,
                    desteteList
            );
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

}
