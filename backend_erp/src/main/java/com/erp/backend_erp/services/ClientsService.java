package com.erp.backend_erp.services;

import org.springframework.data.domain.PageImpl;

import com.erp.backend_erp.dto.clients.ClientsDto;
import com.erp.backend_erp.dto.clients.ClientsTableDto;
import com.erp.backend_erp.dto.clients.CreateClientsDto;
import com.erp.backend_erp.dto.clients.UpdateClientsDto;
import com.erp.backend_erp.util.PageableDto;

public interface  ClientsService {
    ClientsDto create(CreateClientsDto createClientsDto);
    Boolean update(UpdateClientsDto updateClientsDto);
    PageImpl<ClientsTableDto> pageClients(PageableDto<Object> pageableDto);
}
