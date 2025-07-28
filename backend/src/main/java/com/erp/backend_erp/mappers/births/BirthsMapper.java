package com.erp.backend_erp.mappers.births;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.births.BirthsDto;
import com.erp.backend_erp.dto.births.CreateBirthsDto;
import com.erp.backend_erp.dto.births.UpdateBirthsDto;
import com.erp.backend_erp.entity.births.BirthsEntity;

@Mapper(componentModel = "spring")
public interface  BirthsMapper {
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "id_vaca", source = "dto.tipo_vaca"),
        @Mapping(target = "id_toro", source = "dto.nombre_toro"),
        @Mapping(target = "fecha_nacimiento", source = "dto.fecha_nacimiento"),
        @Mapping(target = "numeroCria", source = "dto.numero_cria"),
        @Mapping(target = "sexo", source = "dto.sexo"),
        @Mapping(target = "color_cria", source = "dto.color_cria"),
        @Mapping(target = "observaciones", source = "dto.observaciones"),
        @Mapping(target = "farmId", source = "dto.farmId"),
        @Mapping(target = "peso_cria", source = "dto.peso_cria")
    })
    BirthsEntity createToEntity(CreateBirthsDto dto);

    @Mappings({
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "tipo_vaca", source = "entity.id_vaca"),
        @Mapping(target = "nombre_toro", source = "entity.id_toro"),
        @Mapping(target = "fecha_nacimiento", source = "entity.fecha_nacimiento"),
        @Mapping(target = "numero_cria", source = "entity.numeroCria"),
        @Mapping(target = "sexo", source = "entity.sexo"),
        @Mapping(target = "color_cria", source = "entity.color_cria"),
        @Mapping(target = "observaciones", source = "entity.observaciones"),
        @Mapping(target = "farmId", source = "entity.farmId"),
        @Mapping(target = "peso_cria", source = "entity.peso_cria")
    })
    BirthsDto toDto(BirthsEntity entity);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "id_vaca", source = "dto.tipo_vaca"),
        @Mapping(target = "id_toro", source = "dto.nombre_toro"),
        @Mapping(target = "fecha_nacimiento", source = "dto.fecha_nacimiento"),
        @Mapping(target = "numeroCria", source = "dto.numero_cria"),
        @Mapping(target = "sexo", source = "dto.sexo"),
        @Mapping(target = "color_cria", source = "dto.color_cria"),
        @Mapping(target = "observaciones", source = "dto.observaciones"),
        @Mapping(target = "peso_cria", source = "dto.peso_cria")

    })
    void updateEntityFromDto(UpdateBirthsDto dto, @MappingTarget BirthsEntity entity);
}
