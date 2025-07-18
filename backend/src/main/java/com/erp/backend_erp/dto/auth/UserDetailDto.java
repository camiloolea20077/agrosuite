package com.erp.backend_erp.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailDto {
    private Integer id;
    private String name;
    private String email;
    private Long farm;
    private String role;
    private String farm_name;
}
