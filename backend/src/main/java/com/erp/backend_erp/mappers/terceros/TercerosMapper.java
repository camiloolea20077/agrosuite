package com.erp.backend_erp.mappers.terceros;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.terceros.CreateTerceroDto;
import com.erp.backend_erp.dto.terceros.TerceroDto;
import com.erp.backend_erp.dto.terceros.UpdateTerceroDto;
import com.erp.backend_erp.entity.terceros.TercerosEntity;

@Mapper(componentModel = "spring")
public interface TercerosMapper {
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "tipoIdentificacion", source = "dto.tipoIdentificacion"),
        @Mapping(target = "numeroIdentificacion", source = "dto.numeroIdentificacion"),
        @Mapping(target = "nombreRazonSocial", source = "dto.nombreRazonSocial"),
        @Mapping(target = "telefono", source = "dto.telefono"),
        @Mapping(target = "correo", source = "dto.correo"),
        @Mapping(target = "direccion", source = "dto.direccion"),
        @Mapping(target = "farmId", source = "dto.farmId")
    })
    TercerosEntity createToEntity(CreateTerceroDto dto);

    @Mappings({
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "tipoIdentificacion", source = "entity.tipoIdentificacion"),
        @Mapping(target = "numeroIdentificacion", source = "entity.numeroIdentificacion"),
        @Mapping(target = "nombreRazonSocial", source = "entity.nombreRazonSocial"),
        @Mapping(target = "telefono", source = "entity.telefono"),
        @Mapping(target = "correo", source = "entity.correo"),
        @Mapping(target = "direccion", source = "entity.direccion"),
        @Mapping(target = "farmId", source = "entity.farmId")
    })
    TerceroDto toDto(TercerosEntity entity);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "tipoIdentificacion", source = "dto.tipoIdentificacion"),
        @Mapping(target = "numeroIdentificacion", source = "dto.numeroIdentificacion"),
        @Mapping(target = "nombreRazonSocial", source = "dto.nombreRazonSocial"),
        @Mapping(target = "telefono", source = "dto.telefono"),
        @Mapping(target = "correo", source = "dto.correo"),
        @Mapping(target = "direccion", source = "dto.direccion")
    })
    void updateEntityFromDto(UpdateTerceroDto dto, @MappingTarget TercerosEntity entity);
}
