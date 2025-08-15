package com.erp.backend_erp.mappers.tiposMovimientos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.inventory.CreateTiposMovimientosDto;
import com.erp.backend_erp.dto.inventory.TiposMovimientosDto;
import com.erp.backend_erp.dto.inventory.UpdateTiposMovimientosDto;
import com.erp.backend_erp.entity.inventory.TiposMovimientosEntity;

@Mapper(componentModel = "spring")
public interface TiposMovimientosMapper {
@Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "codigo", source = "dto.codigo"),
        @Mapping(target = "nombre", source = "dto.nombre"),
        @Mapping(target = "descripcion", source = "dto.descripcion"),
        @Mapping(target = "esEntrada", source = "dto.esEntrada"),
        @Mapping(target = "esSalida", source = "dto.esSalida"),
        @Mapping(target = "requiereEmpleado", source = "dto.requiereEmpleado"),
        @Mapping(target = "requiereAprobacion", source = "dto.requiereAprobacion"),
        @Mapping(target = "activo", source = "dto.activo")
    })
    TiposMovimientosEntity createToEntity(CreateTiposMovimientosDto dto);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "codigo", source = "dto.codigo"),
        @Mapping(target = "nombre", source = "dto.nombre"),
        @Mapping(target = "descripcion", source = "dto.descripcion"),
        @Mapping(target = "esEntrada", source = "dto.esEntrada"),
        @Mapping(target = "esSalida", source = "dto.esSalida"),
        @Mapping(target = "requiereEmpleado", source = "dto.requiereEmpleado"),
        @Mapping(target = "requiereAprobacion", source = "dto.requiereAprobacion"),
        @Mapping(target = "activo", source = "dto.activo")
    })
    void updateEntityFromDto(UpdateTiposMovimientosDto dto, @MappingTarget TiposMovimientosEntity entity);

    @Mappings({
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "codigo", source = "entity.codigo"),
        @Mapping(target = "nombre", source = "entity.nombre"),
        @Mapping(target = "descripcion", source = "entity.descripcion"),
        @Mapping(target = "esEntrada", source = "entity.esEntrada"),
        @Mapping(target = "esSalida", source = "entity.esSalida"),
        @Mapping(target = "requiereEmpleado", source = "entity.requiereEmpleado"),
        @Mapping(target = "requiereAprobacion", source = "entity.requiereAprobacion"),
        @Mapping(target = "activo", source = "entity.activo")
    })
    TiposMovimientosDto toDto(TiposMovimientosEntity entity);
}
