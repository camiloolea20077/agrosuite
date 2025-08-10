package com.erp.backend_erp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.backend_erp.dto.emails.EmailRequestDto;
import com.erp.backend_erp.services.EmailService;
import com.erp.backend_erp.util.ApiResponse;

@RestController
@RequestMapping("/emails")
public class EmailController {
    
    @Autowired
    private EmailService emailService;

@PostMapping("/send-email")
public ResponseEntity<ApiResponse<Object>> enviarCorreo(@Valid @RequestBody EmailRequestDto request) throws Exception {
    try {
        emailService.enviarCorreo(request);
        ApiResponse<Object> response = new ApiResponse<>(
            HttpStatus.OK.value(),
            "Correo enviado exitosamente",
            false,
            null
        );
        return ResponseEntity.ok(response);
    } catch (Exception ex) {
        throw ex;
    }
}

}
