package com.erp.backend_erp.mappers.suppliers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.suppliers.CreateSuppliersDto;
import com.erp.backend_erp.dto.suppliers.SuppliersDto;
import com.erp.backend_erp.dto.suppliers.UpdateSuppliersDto;
import com.erp.backend_erp.entity.inventory.SuppliersEntity;

@Mapper(componentModel = "spring")
public interface SuppliersMapper {
@Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "codigo", source = "dto.codigo"),
        @Mapping(target = "nombre", source = "dto.nombre"),
        @Mapping(target = "contacto", source = "dto.contacto"),
        @Mapping(target = "telefono", source = "dto.telefono"),
        @Mapping(target = "email", source = "dto.email"),
        @Mapping(target = "direccion", source = "dto.direccion"),
        @Mapping(target = "activo", source = "dto.activo")
    })
    SuppliersEntity createToEntity(CreateSuppliersDto dto);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "codigo", source = "dto.codigo"),
        @Mapping(target = "nombre", source = "dto.nombre"),
        @Mapping(target = "contacto", source = "dto.contacto"),
        @Mapping(target = "telefono", source = "dto.telefono"),
        @Mapping(target = "email", source = "dto.email"),
        @Mapping(target = "direccion", source = "dto.direccion"),
        @Mapping(target = "activo", source = "dto.activo")
    })
    void updateEntityFromDto(UpdateSuppliersDto dto, @MappingTarget SuppliersEntity entity);

    @Mappings({
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "codigo", source = "entity.codigo"),
        @Mapping(target = "nombre", source = "entity.nombre"),
        @Mapping(target = "contacto", source = "entity.contacto"),
        @Mapping(target = "telefono", source = "entity.telefono"),
        @Mapping(target = "email", source = "entity.email"),
        @Mapping(target = "direccion", source = "entity.direccion"),
        @Mapping(target = "activo", source = "entity.activo")
    })
    SuppliersDto toDto(SuppliersEntity entity);
}
