package com.erp.backend_erp.mappers.users;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.users.CreateUserDto;
import com.erp.backend_erp.dto.users.UpdateUserDto;
import com.erp.backend_erp.dto.users.UserDto;
import com.erp.backend_erp.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMappers {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "created_at", ignore = true),
            @Mapping(target = "updated_at", ignore = true),
            @Mapping(target = "role", ignore = true),
            @Mapping(target = "email", source = "dto.email"),
            @Mapping(target = "password", source = "dto.password"),
            @Mapping(target = "name", source = "dto.name"),
            @Mapping(target = "company_id", source = "dto.company_id"),
            @Mapping(target = "username", source = "dto.username"),
    })
    UserEntity createToEntity(CreateUserDto dto);

    @AfterMapping
    default void handlePassword(UpdateUserDto dto,
            @MappingTarget UserEntity entity) {
        if (dto.getPassword() != null) {
            entity.setPassword(dto.getPassword());
        }
    }

    @Mappings({
            @Mapping(target = "id", source = "entity.id"),
            @Mapping(target = "email", source = "entity.email"),
            @Mapping(target = "password", source = "entity.password"),
            @Mapping(target = "name", source = "entity.name"),
            @Mapping(target = "role_id", source = "entity.role.id"),
            @Mapping(target = "company_id", source = "entity.company_id"),
            @Mapping(target = "username", source = "entity.username"),
    })
    UserDto toDto(UserEntity entity);
}
