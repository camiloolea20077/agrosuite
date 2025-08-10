package com.erp.backend_erp.dto.emails;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDto {
    private String to;
    private String subject;
    private String template;
    private Map<String, String> variables;


}
