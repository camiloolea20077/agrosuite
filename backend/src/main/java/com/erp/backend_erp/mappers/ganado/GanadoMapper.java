package com.erp.backend_erp.mappers.ganado;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.ganado.CreateGanadoDto;
import com.erp.backend_erp.dto.ganado.GanadoDto;
import com.erp.backend_erp.dto.ganado.UpdateGanadoDto;
import com.erp.backend_erp.entity.ganado.GanadoEntity;

@Mapper(componentModel = "spring")
public interface GanadoMapper {
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "tipo_ganado", source = "dto.tipo_ganado"),
        @Mapping(target = "numero_ganado", source = "dto.numero_ganado"),
        @Mapping(target = "sexo", source = "dto.sexo"),
        @Mapping(target = "color", source = "dto.color"),
        @Mapping(target = "peso", source = "dto.peso"),
        @Mapping(target = "fecha_nacimiento", source = "dto.fecha_nacimiento"),
        @Mapping(target = "lote_ganado", source = "dto.lote_ganado"),
        @Mapping(target = "observaciones", source = "dto.observaciones"),
        @Mapping(target = "activo", source = "dto.activo"),
        @Mapping(target = "farmId", source = "dto.farmId"),
        @Mapping(target = "tipo_animal", source = "dto.tipo_animal"),
        @Mapping(target = "embarazada", source = "dto.embarazada"),
        @Mapping(target = "fecha_embarazo", source = "dto.fecha_embarazo"),
    })
    GanadoEntity createToEntity(CreateGanadoDto dto);

    @Mappings
    ({
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "tipo_ganado", source = "entity.tipo_ganado"),
        @Mapping(target = "numero_ganado", source = "entity.numero_ganado"),
        @Mapping(target = "sexo", source = "entity.sexo"),
        @Mapping(target = "color", source = "entity.color"),
        @Mapping(target = "peso", source = "entity.peso"),
        @Mapping(target = "fecha_nacimiento", source = "entity.fecha_nacimiento"),
        @Mapping(target = "lote_ganado", source = "entity.lote_ganado"),
        @Mapping(target = "observaciones", source = "entity.observaciones"),
        @Mapping(target = "activo", source = "entity.activo"),
        @Mapping(target = "farmId", source = "entity.farmId"),
        @Mapping(target = "tipo_animal", source = "entity.tipo_animal"),
        @Mapping(target = "embarazada", source = "entity.embarazada"),
        @Mapping(target = "fecha_embarazo", source = "entity.fecha_embarazo"),
    })
    GanadoDto toDto(GanadoEntity entity);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "tipo_ganado", source = "dto.tipo_ganado"),
        @Mapping(target = "numero_ganado", source = "dto.numero_ganado"),
        @Mapping(target = "sexo", source = "dto.sexo"),
        @Mapping(target = "color", source = "dto.color"),
        @Mapping(target = "peso", source = "dto.peso"),
        @Mapping(target = "fecha_nacimiento", source = "dto.fecha_nacimiento"),
        @Mapping(target = "lote_ganado", source = "dto.lote_ganado"),
        @Mapping(target = "observaciones", source = "dto.observaciones"),
        @Mapping(target = "activo", source = "dto.activo"),
        @Mapping(target = "farmId", source = "dto.farmId"),
        @Mapping(target = "tipo_animal", source = "dto.tipo_animal"),
        @Mapping(target = "embarazada", source = "dto.embarazada"),
        @Mapping(target = "fecha_embarazo", source = "dto.fecha_embarazo"),
    })
    void updateEntityFromDto(UpdateGanadoDto dto, @MappingTarget GanadoEntity entity);
}
