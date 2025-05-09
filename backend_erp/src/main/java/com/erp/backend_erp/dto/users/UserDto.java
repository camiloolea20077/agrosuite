package com.erp.backend_erp.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;
    private Long role_id;
    private Integer company_id;
}
