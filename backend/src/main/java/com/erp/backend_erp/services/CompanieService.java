package com.erp.backend_erp.services;

import com.erp.backend_erp.dto.companies.CompaniesDto;
import com.erp.backend_erp.dto.companies.CreateCompaniesDto;
import com.erp.backend_erp.dto.companies.UpdateCompaniesDto;

public interface CompanieService {
    CompaniesDto create(CreateCompaniesDto companiesDto);
    Boolean update(UpdateCompaniesDto updateCompaniesDto);
    Boolean delete(Long id);
}
