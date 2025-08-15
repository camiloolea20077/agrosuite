package com.erp.backend_erp.mappers.estadosInventario;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.inventory.CreateEstadosInventarioDto;
import com.erp.backend_erp.dto.inventory.EstadosInventarioDto;
import com.erp.backend_erp.dto.inventory.UpdateEstadosInventarioDto;
import com.erp.backend_erp.entity.inventory.EstadosInventarioEntity;

@Mapper(componentModel = "spring")
public interface EstadosInventarioMapper {
@Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "codigo", source = "dto.codigo"),
        @Mapping(target = "nombre", source = "dto.nombre"),
        @Mapping(target = "descripcion", source = "dto.descripcion"),
        @Mapping(target = "activo", source = "dto.activo")
    })
    EstadosInventarioEntity createToEntity(CreateEstadosInventarioDto dto);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "codigo", source = "dto.codigo"),
        @Mapping(target = "nombre", source = "dto.nombre"),
        @Mapping(target = "descripcion", source = "dto.descripcion"),
        @Mapping(target = "activo", source = "dto.activo")
    })
    void updateEntityFromDto(UpdateEstadosInventarioDto dto, @MappingTarget EstadosInventarioEntity entity);

    @Mappings({
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "codigo", source = "entity.codigo"),
        @Mapping(target = "nombre", source = "entity.nombre"),
        @Mapping(target = "descripcion", source = "entity.descripcion"),
        @Mapping(target = "activo", source = "entity.activo")
    })
    EstadosInventarioDto toDto(EstadosInventarioEntity entity);
}
