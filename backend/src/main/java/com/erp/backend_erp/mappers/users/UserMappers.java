package com.erp.backend_erp.mappers.users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.users.CreateUserDto;
import com.erp.backend_erp.dto.users.UserDto;
import com.erp.backend_erp.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMappers {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "created_at", ignore = true),
            @Mapping(target = "updated_at", ignore = true),
            @Mapping(target = "role_id", source="dto.role_id"),
            @Mapping(target = "email", source = "dto.email"),
            @Mapping(target = "nombre_completo", source = "dto.name"),
            @Mapping(target = "password", source = "dto.password"),
            @Mapping(target = "username", source = "dto.username"),
            @Mapping(target = "activo", source = "dto.active"),
            @Mapping(target = "farm_id", source = "dto.farmId")
    })
    UserEntity createToEntity(CreateUserDto dto);
    @Mappings({
            @Mapping(target = "id", source = "entity.id"),
            @Mapping(target = "email", source = "entity.email"),
            @Mapping(target = "password", source = "entity.password"),
            @Mapping(target = "name", source = "entity.nombre_completo"),
            @Mapping(target = "role_id", source = "entity.role_id"),
            @Mapping(target = "active", source = "entity.activo"),
            @Mapping(target = "username", source = "entity.username"),
            @Mapping(target = "farm_id", source = "entity.farm_id")
    })
    UserDto toDto(UserEntity entity);
}
