package com.erp.backend_erp.mappers.clients;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.clients.ClientsDto;
import com.erp.backend_erp.dto.clients.CreateClientsDto;
import com.erp.backend_erp.dto.clients.UpdateClientsDto;
import com.erp.backend_erp.entity.clients.ClientsEntity;

@Mapper(componentModel = "spring")
public interface ClientsMapper {
        @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "name", source = "dto.name"),
        @Mapping(target = "company_id", source = "dto.company_id"),
        @Mapping(target = "email", source = "dto.email"),
        @Mapping(target = "phone", source = "dto.phone"),
        @Mapping(target = "document_type", source = "dto.document_type"),
        @Mapping(target = "document", source = "dto.document"),
        })
        ClientsEntity createToEntity(CreateClientsDto dto);
        @Mappings({ @Mapping(target = "id", source = "dto.id"), @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true), @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "name", source = "dto.name"),
        @Mapping(target = "company_id", source = "dto.company_id"),
        @Mapping(target = "email", source = "dto.email"),
        @Mapping(target = "phone", source = "dto.phone"),
        @Mapping(target = "document_type", source = "dto.document_type"),
        @Mapping(target = "document", source = "dto.document"),
			})
	void updateEntityFromDto(UpdateClientsDto dto, @MappingTarget ClientsEntity entity);

        @Mappings({
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "name", source = "entity.name"),
        @Mapping(target = "company_id", source = "entity.company_id"),
        @Mapping(target = "email", source = "entity.email"),
        @Mapping(target = "phone", source = "entity.phone"),
        @Mapping(target = "document_type", source = "entity.document_type"),
        @Mapping(target = "document", source = "entity.document"),
        })
        ClientsDto toDto(ClientsEntity entity);
}
