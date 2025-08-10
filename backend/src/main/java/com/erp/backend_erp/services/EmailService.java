package com.erp.backend_erp.services;

import com.erp.backend_erp.dto.emails.EmailRequestDto;

public interface EmailService {
    void enviarCorreo(EmailRequestDto request) throws Exception;

}
