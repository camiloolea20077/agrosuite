package com.erp.backend_erp.mappers.roles;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.roles.CreateRoleDto;
import com.erp.backend_erp.dto.roles.RoleDto;
import com.erp.backend_erp.dto.roles.UpdateRoleDto;
import com.erp.backend_erp.entity.role.RoleEntity;

@Mapper(componentModel = "spring")
public interface RolesMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "created_at", ignore = true),
            @Mapping(target = "updated_at", ignore = true),
            @Mapping(target = "name", source = "dto.name"),
            @Mapping(target = "description", source = "dto.description"),
    })
    RoleEntity createToEntity(CreateRoleDto dto);
    @Mappings({ @Mapping(target = "id", source = "dto.id"), @Mapping(target = "created_at", ignore = true),
			@Mapping(target = "updated_at", ignore = true), @Mapping(target = "deleted_at", ignore = true),
			@Mapping(target = "name", source = "dto.name"),
			@Mapping(target = "description", source = "dto.description"),
			})
	void updateEntityFromDto(UpdateRoleDto dto, @MappingTarget RoleEntity entity);

    @Mappings({
            @Mapping(target = "id", source = "entity.id"),
            @Mapping(target = "name", source = "entity.name"),
            @Mapping(target = "description", source = "entity.description"),
    })
    RoleDto toDto(RoleEntity entity);
}
