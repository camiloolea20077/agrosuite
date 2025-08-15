package com.erp.backend_erp.mappers.tiposInsumos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.insumos.CreateInsumoRequestsDto;
import com.erp.backend_erp.dto.insumos.InsumoRequestsDto;
import com.erp.backend_erp.dto.insumos.UpdateInsumoRequestsDto;
import com.erp.backend_erp.entity.inventory.InsumoRequestsEntity;

@Mapper(componentModel = "spring")
public interface InsumoRequestsMapper {
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "procesadoPor", ignore = true),
        @Mapping(target = "fechaProcesamiento", ignore = true),
        @Mapping(target = "numeroSolicitud", source = "dto.numeroSolicitud"),
        @Mapping(target = "farmId", source = "dto.farmId"),
        @Mapping(target = "fechaSolicitud", source = "dto.fechaSolicitud"),
        @Mapping(target = "fechaNecesaria", source = "dto.fechaNecesaria"),
        @Mapping(target = "prioridad", source = "dto.prioridad"),
        @Mapping(target = "justificacion", source = "dto.justificacion"),
        @Mapping(target = "observaciones", source = "dto.observaciones"),
        @Mapping(target = "estado", source = "dto.estado"),
        @Mapping(target = "solicitadoPor", source = "dto.solicitadoPor")
    })
    InsumoRequestsEntity createToEntity(CreateInsumoRequestsDto dto);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "solicitadoPor", ignore = true),
        @Mapping(target = "numeroSolicitud", source = "dto.numeroSolicitud"),
        @Mapping(target = "farmId", source = "dto.farmId"),
        @Mapping(target = "fechaSolicitud", source = "dto.fechaSolicitud"),
        @Mapping(target = "fechaNecesaria", source = "dto.fechaNecesaria"),
        @Mapping(target = "prioridad", source = "dto.prioridad"),
        @Mapping(target = "justificacion", source = "dto.justificacion"),
        @Mapping(target = "observaciones", source = "dto.observaciones"),
        @Mapping(target = "estado", source = "dto.estado"),
        @Mapping(target = "procesadoPor", source = "dto.procesadoPor"),
        @Mapping(target = "fechaProcesamiento", source = "dto.fechaProcesamiento")
    })
    void updateEntityFromDto(UpdateInsumoRequestsDto dto, @MappingTarget InsumoRequestsEntity entity);

    InsumoRequestsDto toDto(InsumoRequestsEntity entity);
}
