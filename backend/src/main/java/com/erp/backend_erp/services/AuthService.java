package com.erp.backend_erp.services;

import com.erp.backend_erp.dto.auth.AuthDto;
import com.erp.backend_erp.dto.auth.LoginDto;

public interface AuthService {
    public AuthDto login(LoginDto loginDto);
    // AuthDto register(RegisterRequestDto dto);

}
