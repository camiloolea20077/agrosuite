package com.erp.backend_erp.mappers.company;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.companies.CompaniesDto;
import com.erp.backend_erp.dto.companies.CreateCompaniesDto;
import com.erp.backend_erp.dto.companies.UpdateCompaniesDto;
import com.erp.backend_erp.entity.companies.CompaniesEntity;


@Mapper(componentModel = "spring")
public interface CompanyMapper {
        @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "name", source = "dto.name"),
        @Mapping(target = "plan", source = "dto.plan"),
        @Mapping(target = "nit", source = "dto.nit"),
        @Mapping(target = "email", source = "dto.email"),
        @Mapping(target = "address", source = "dto.address"),
        @Mapping(target = "phone", source = "dto.phone"),
        })
        CompaniesEntity createToEntity(CreateCompaniesDto dto);
        @Mappings({ @Mapping(target = "id", source = "dto.id"), @Mapping(target = "created_at", ignore = true),
	@Mapping(target = "updated_at", ignore = true), @Mapping(target = "deleted_at", ignore = true),
	@Mapping(target = "name", source = "dto.name"),
	@Mapping(target = "email", source = "dto.email"),
        @Mapping(target = "address", source = "dto.address"),
        @Mapping(target = "phone", source = "dto.phone"),
        @Mapping(target = "plan", source = "dto.plan"),
			})
	void updateEntityFromDto(UpdateCompaniesDto dto, @MappingTarget CompaniesEntity entity);

        @Mappings({
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "name", source = "entity.name"),
        @Mapping(target = "plan", source = "entity.plan"),
        @Mapping(target = "nit", source = "entity.nit"),
        @Mapping(target = "email", source = "entity.email"),
        @Mapping(target = "address", source = "entity.address"),
        @Mapping(target = "phone", source = "entity.phone"),
        })
        CompaniesDto toDto(CompaniesEntity entity);
}
