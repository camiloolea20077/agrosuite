package com.erp.backend_erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.births.DashboardData;
import com.erp.backend_erp.services.BirthsService;
import com.erp.backend_erp.util.ApiResponse;

@RestController
@RequestMapping("/dashboard")
public class DashboardDataController {

    @Autowired
    BirthsService birthsService;

@GetMapping("/data")
public ResponseEntity<ApiResponse<Object>> findListForId() {
    try {
        DashboardData dashboardData = this.birthsService.getDashboardData();
        ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, dashboardData);
        return ResponseEntity.ok(response);
    } catch (Exception ex) {
        throw ex;
    }
}

}
