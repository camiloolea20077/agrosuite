package com.erp.backend_erp.dto.clients;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientsTableDto {
    private String company_name;
    private String name;
    private String email;
    private String phone;
    private String document_type;
    private String document;
}
