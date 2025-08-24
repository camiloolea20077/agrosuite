package com.erp.backend_erp.mappers.cattleDeath;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.cattleDeath.CattleDeathDto;
import com.erp.backend_erp.dto.cattleDeath.CreateCattleDeathDto;
import com.erp.backend_erp.entity.deathCattle.CattleDeathEntity;

@Mapper(componentModel = "spring")
public interface CattleDeathMapper {
    
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "cattleId", source = "dto.cattleId"),
        @Mapping(target = "birthId", source = "dto.birthId"),
        @Mapping(target = "fechaMuerte", source = "dto.fechaMuerte"),
        @Mapping(target = "pesoMuerte", source = "dto.pesoMuerte"),
        @Mapping(target = "motivoMuerte", source = "dto.motivoMuerte"),
        @Mapping(target = "descripcionDetallada", source = "dto.descripcionDetallada"),
        @Mapping(target = "causaCategoria", source = "dto.causaCategoria"),
        @Mapping(target = "farmId", source = "dto.farmId"),
        @Mapping(target = "usuarioRegistro", source = "dto.usuarioRegistro"),
    })
    CattleDeathEntity toEntity(CreateCattleDeathDto dto);

    @Mappings({
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "cattleId", source = "entity.cattleId"),
        @Mapping(target = "birthId", source = "entity.birthId"),
        @Mapping(target = "fechaMuerte", source = "entity.fechaMuerte"),
        @Mapping(target = "pesoMuerte", source = "entity.pesoMuerte"),
        @Mapping(target = "motivoMuerte", source = "entity.motivoMuerte"),
        @Mapping(target = "descripcionDetallada", source = "entity.descripcionDetallada"),
        @Mapping(target = "causaCategoria", source = "entity.causaCategoria"),
        @Mapping(target = "farmId", source = "entity.farmId"),
        @Mapping(target = "usuarioRegistro", source = "entity.usuarioRegistro"),
    })
    CattleDeathDto toDto(CattleDeathEntity entity);

    @Mappings({
        @Mapping(target = "cattleId", source = "entity.cattleId"),
        @Mapping(target = "birthId", source = "entity.birthId"),
        @Mapping(target = "fechaMuerte", source = "entity.fechaMuerte"),
        @Mapping(target = "pesoMuerte", source = "entity.pesoMuerte"),
        @Mapping(target = "motivoMuerte", source = "entity.motivoMuerte"),
        @Mapping(target = "descripcionDetallada", source = "entity.descripcionDetallada"),
        @Mapping(target = "causaCategoria", source = "entity.causaCategoria"),
        @Mapping(target = "farmId", source = "entity.farmId"),
        @Mapping(target = "usuarioRegistro", source = "entity.usuarioRegistro"),
    })
    CreateCattleDeathDto toUpdateDto(CattleDeathEntity entity);
}
