package com.erp.backend_erp.dto.employees;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmployeesDto {
    private String nombre;
    private String identificacion;
    private String cargo;
    private String fecha_ingreso;
    private Long activo;
    private Long farmId;
}
