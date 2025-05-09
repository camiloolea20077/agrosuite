package com.erp.backend_erp.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    public String email;
    public String password;
}
