package com.erp.backend_erp.dto.clients;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientsDto {
    private Long id;
    private Long company_id;
    private String name;
    private String email;
    private String phone;
    private String document_type;
    private String document;
}
