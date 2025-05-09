package com.erp.backend_erp.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {
    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;
    private Integer role;
    private Integer company_id;
}
