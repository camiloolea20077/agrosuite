package com.erp.backend_erp.services;

import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.suppliers.CreateSuppliersDto;
import com.erp.backend_erp.dto.suppliers.SuppliersDto;
import com.erp.backend_erp.dto.suppliers.SuppliersTableDto;
import com.erp.backend_erp.dto.suppliers.UpdateSuppliersDto;
import com.erp.backend_erp.util.PageableDto;

public interface SuppliersService {
    SuppliersDto create(CreateSuppliersDto createDto);
    List<SuppliersDto> createAll(List<CreateSuppliersDto> dtoList);
    Boolean update(UpdateSuppliersDto updateDto);
    Boolean delete(Long id);
    Boolean activate(Long id);
    SuppliersDto findById(Long id);
    List<SuppliersDto> findAllActive();
    PageImpl<SuppliersTableDto> getPage(PageableDto<Object> pageableDto);
}