package com.erp.backend_erp.mappers.register;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.auth.RegisterRequestDto;
import com.erp.backend_erp.dto.companies.CreateCompaniesDto;
import com.erp.backend_erp.dto.users.CreateUserDto;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    @Mappings({
        @Mapping(target = "name", source = "dto.company_name"),
        @Mapping(target = "plan", source = "dto.plan"),
        @Mapping(target = "nit", source = "dto.nit"),
        @Mapping(target = "email", source = "dto.email"),
        @Mapping(target = "address", source = "dto.address"),
        @Mapping(target = "phone", source = "dto.phone")
    })
    CreateCompaniesDto toCreateCompanyDto(RegisterRequestDto dto);

    @Mappings({
        @Mapping(target = "name", source = "dto.full_name"),
        @Mapping(target = "email", source = "dto.admin_email"),
        @Mapping(target = "password", source = "dto.password"),
        @Mapping(target = "role_id", ignore = true),
        @Mapping(target = "active", ignore = true)
    })
    CreateUserDto toCreateUserDto(RegisterRequestDto dto);
}
