package com.erp.backend_erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.listElements.CattleElementsDto;
import com.erp.backend_erp.dto.listElements.FarmsElementsDto;
import com.erp.backend_erp.dto.listElements.RolesElementsDto;
import com.erp.backend_erp.dto.listElements.UsersElementsDto;
import com.erp.backend_erp.services.ListElementService;
import com.erp.backend_erp.util.ApiResponse;

@RestController
@RequestMapping("/list-elements")
public class ListElementController {

    @Autowired
    ListElementService listElementService;

    @GetMapping("/list-for-cattle-male")
    public ResponseEntity<ApiResponse<Object>> findListForId(@RequestHeader("farmId") Long farmId) {
        try {
            List<CattleElementsDto> list = this.listElementService.findListForId(farmId);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, list);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @GetMapping("/list-for-cattle-female")
    public ResponseEntity<ApiResponse<Object>> findListForIdFemale(@RequestHeader("farmId") Long farmId) {
        try {
            List<CattleElementsDto> list = this.listElementService.findListForIdFemale(farmId);
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, list);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/list-farms")
    public ResponseEntity<ApiResponse<Object>> getFarms() {
        try {
            List<FarmsElementsDto> list = this.listElementService.getFarms();
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, list);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/list-roles")
    public ResponseEntity<ApiResponse<Object>> getRoles() {
        try {
            List<RolesElementsDto> list = this.listElementService.getRoles();
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, list);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @GetMapping("/list-users")
    public ResponseEntity<ApiResponse<Object>> getUsers() {
        try {
            List<UsersElementsDto> list = this.listElementService.getUsers();
            ApiResponse<Object> response = new ApiResponse<>(HttpStatus.OK.value(), "", false, list);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
