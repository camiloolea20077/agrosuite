package com.erp.backend_erp.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDto {
    private UserDetailDto user;
    private String token;
}
