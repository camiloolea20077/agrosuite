package com.erp.backend_erp.services;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.employees.CreateEmployeesDto;
import com.erp.backend_erp.dto.employees.EmployeesDto;
import com.erp.backend_erp.dto.employees.EmployeesTableDto;
import com.erp.backend_erp.dto.employees.UpdateEmployeesDto;
import com.erp.backend_erp.util.PageableDto;

public interface EmployeesService {
    EmployeesDto create(CreateEmployeesDto createEmployeesDto);
    Boolean delete(Long id);
    Boolean update(UpdateEmployeesDto updateEmployeesDto);
    EmployeesDto findById(Long id, Long farmId);
    PageImpl<EmployeesTableDto> pageEmployees(PageableDto<Object> pageableDto);
}
