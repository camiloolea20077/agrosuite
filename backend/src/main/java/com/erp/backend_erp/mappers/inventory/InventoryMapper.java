package com.erp.backend_erp.mappers.inventory;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.inventory.CreateInventoryDto;
import com.erp.backend_erp.dto.inventory.InventoryDto;
import com.erp.backend_erp.dto.inventory.UpdateInventoryDto;
import com.erp.backend_erp.entity.inventory.InventoryEntity;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
@Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "updatedBy", ignore = true),
        @Mapping(target = "codigoInterno", source = "dto.codigoInterno"),
        @Mapping(target = "nombreInsumo", source = "dto.nombreInsumo"),
        @Mapping(target = "descripcion", source = "dto.descripcion"),
        @Mapping(target = "marca", source = "dto.marca"),
        @Mapping(target = "tipoInsumoId", source = "dto.tipoInsumoId"),
        @Mapping(target = "unidadMedida", source = "dto.unidadMedida"),
        @Mapping(target = "unidadCompra", source = "dto.unidadCompra"),
        @Mapping(target = "factorConversion", source = "dto.factorConversion"),
        @Mapping(target = "cantidadActual", source = "dto.cantidadActual"),
        @Mapping(target = "cantidadMinima", source = "dto.cantidadMinima"),
        @Mapping(target = "puntoReorden", source = "dto.puntoReorden"),
        @Mapping(target = "cantidadReservada", source = "dto.cantidadReservada"),
        @Mapping(target = "ubicacionAlmacen", source = "dto.ubicacionAlmacen"),
        @Mapping(target = "esPeligroso", source = "dto.esPeligroso"),
        @Mapping(target = "requiereCuidadoEspecial", source = "dto.requiereCuidadoEspecial"),
        @Mapping(target = "notas", source = "dto.notas"),
        @Mapping(target = "estadoId", source = "dto.estadoId"),
        @Mapping(target = "farmId", source = "dto.farmId"),
        @Mapping(target = "createdBy", source = "dto.createdBy")
    })
    InventoryEntity createToEntity(CreateInventoryDto dto);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "farmId", ignore = true),
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "codigoInterno", source = "dto.codigoInterno"),
        @Mapping(target = "nombreInsumo", source = "dto.nombreInsumo"),
        @Mapping(target = "descripcion", source = "dto.descripcion"),
        @Mapping(target = "marca", source = "dto.marca"),
        @Mapping(target = "tipoInsumoId", source = "dto.tipoInsumoId"),
        @Mapping(target = "unidadMedida", source = "dto.unidadMedida"),
        @Mapping(target = "unidadCompra", source = "dto.unidadCompra"),
        @Mapping(target = "factorConversion", source = "dto.factorConversion"),
        @Mapping(target = "cantidadActual", source = "dto.cantidadActual"),
        @Mapping(target = "cantidadMinima", source = "dto.cantidadMinima"),
        @Mapping(target = "puntoReorden", source = "dto.puntoReorden"),
        @Mapping(target = "cantidadReservada", source = "dto.cantidadReservada"),
        @Mapping(target = "ubicacionAlmacen", source = "dto.ubicacionAlmacen"),
        @Mapping(target = "esPeligroso", source = "dto.esPeligroso"),
        @Mapping(target = "requiereCuidadoEspecial", source = "dto.requiereCuidadoEspecial"),
        @Mapping(target = "notas", source = "dto.notas"),
        @Mapping(target = "estadoId", source = "dto.estadoId"),
        @Mapping(target = "updatedBy", source = "dto.updatedBy")
    })
    void updateEntityFromDto(UpdateInventoryDto dto, @MappingTarget InventoryEntity entity);

    @Mappings({
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "codigoInterno", source = "entity.codigoInterno"),
        @Mapping(target = "nombreInsumo", source = "entity.nombreInsumo"),
        @Mapping(target = "descripcion", source = "entity.descripcion"),
        @Mapping(target = "marca", source = "entity.marca"),
        @Mapping(target = "tipoInsumoId", source = "entity.tipoInsumoId"),
        @Mapping(target = "unidadMedida", source = "entity.unidadMedida"),
        @Mapping(target = "unidadCompra", source = "entity.unidadCompra"),
        @Mapping(target = "factorConversion", source = "entity.factorConversion"),
        @Mapping(target = "cantidadActual", source = "entity.cantidadActual"),
        @Mapping(target = "cantidadMinima", source = "entity.cantidadMinima"),
        @Mapping(target = "puntoReorden", source = "entity.puntoReorden"),
        @Mapping(target = "cantidadReservada", source = "entity.cantidadReservada"),
        @Mapping(target = "ubicacionAlmacen", source = "entity.ubicacionAlmacen"),
        @Mapping(target = "esPeligroso", source = "entity.esPeligroso"),
        @Mapping(target = "requiereCuidadoEspecial", source = "entity.requiereCuidadoEspecial"),
        @Mapping(target = "notas", source = "entity.notas"),
        @Mapping(target = "estadoId", source = "entity.estadoId"),
        @Mapping(target = "farmId", source = "entity.farmId"),
        @Mapping(target = "createdBy", source = "entity.createdBy"),
        @Mapping(target = "updatedBy", source = "entity.updatedBy")
    })
    InventoryDto toDto(InventoryEntity entity);
}
