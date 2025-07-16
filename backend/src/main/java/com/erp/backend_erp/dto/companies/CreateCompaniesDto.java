package com.erp.backend_erp.dto.companies;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCompaniesDto {
    private String name;
    private String nit;
    private String email;
    private String address;
    private String phone;
    private String plan;
}
