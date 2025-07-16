package com.erp.backend_erp.dto.companies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCompaniesDto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String plan;
}
