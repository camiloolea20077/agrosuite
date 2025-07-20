package com.erp.backend_erp.mappers.products;

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
            @Mapping(target = "nombreInsumo", source="dto.nombre_insumo"),
            @Mapping(target = "unidad", source="dto.unidad"),
            @Mapping(target = "cantidad_total", source="dto.cantidad_total"),
            @Mapping(target = "descripcion", source="dto.descripcion"),
            @Mapping(target = "farmId", source = "dto.farmId"),
    })
    InventoryEntity createToEntity(CreateInventoryDto dto);
    @Mappings({ 
            @Mapping(target = "id", source = "dto.id"), 
            @Mapping(target = "created_at", ignore = true),
			@Mapping(target = "updated_at", ignore = true), 
            @Mapping(target = "deleted_at", ignore = true),
            @Mapping(target = "nombreInsumo", source="dto.nombre_insumo"),
            @Mapping(target = "unidad", source="dto.unidad"),
            @Mapping(target = "cantidad_total", source="dto.cantidad_total"),
            @Mapping(target = "descripcion", source="dto.descripcion"),
			})
	void updateEntityFromDto(UpdateInventoryDto dto, @MappingTarget InventoryEntity entity);

    @Mappings({
            @Mapping(target = "id", source = "entity.id"),
            @Mapping(target = "nombre_insumo", source="entity.nombreInsumo"),
            @Mapping(target = "unidad", source="entity.unidad"),
            @Mapping(target = "cantidad_total", source="entity.cantidad_total"),
            @Mapping(target = "descripcion", source="entity.descripcion"),
            @Mapping(target = "farmId", source = "entity.farmId")
    })
    InventoryDto toDto(InventoryEntity entity);
}
