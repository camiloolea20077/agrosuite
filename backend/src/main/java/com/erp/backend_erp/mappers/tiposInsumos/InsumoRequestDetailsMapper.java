package com.erp.backend_erp.mappers.tiposInsumos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.insumos.CreateInsumoRequestDetailsDto;
import com.erp.backend_erp.dto.insumos.InsumoRequestDetailsDto;
import com.erp.backend_erp.dto.insumos.UpdateInsumoRequestDetailsDto;
import com.erp.backend_erp.entity.inventory.InsumoRequestDetailsEntity;

@Mapper(componentModel = "spring")
public interface InsumoRequestDetailsMapper {
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "insumoRequestId", source = "dto.insumoRequestId"),
        @Mapping(target = "inventoryId", source = "dto.inventoryId"),
        @Mapping(target = "cantidadSolicitada", source = "dto.cantidadSolicitada"),
        @Mapping(target = "cantidadAprobada", source = "dto.cantidadAprobada"),
        @Mapping(target = "unidadMedida", source = "dto.unidadMedida"),
        @Mapping(target = "justificacion", source = "dto.justificacion"),
        @Mapping(target = "observaciones", source = "dto.observaciones")
    })
    InsumoRequestDetailsEntity createToEntity(CreateInsumoRequestDetailsDto dto);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "insumoRequestId", source = "dto.insumoRequestId"),
        @Mapping(target = "inventoryId", source = "dto.inventoryId"),
        @Mapping(target = "cantidadSolicitada", source = "dto.cantidadSolicitada"),
        @Mapping(target = "cantidadAprobada", source = "dto.cantidadAprobada"),
        @Mapping(target = "unidadMedida", source = "dto.unidadMedida"),
        @Mapping(target = "justificacion", source = "dto.justificacion"),
        @Mapping(target = "observaciones", source = "dto.observaciones")
    })
    void updateEntityFromDto(UpdateInsumoRequestDetailsDto dto, @MappingTarget InsumoRequestDetailsEntity entity);

    InsumoRequestDetailsDto toDto(InsumoRequestDetailsEntity entity);
}
