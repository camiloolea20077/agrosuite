package com.erp.backend_erp.mappers.orders;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.inventorySuppliers.CreateInventorySuppliersDto;
import com.erp.backend_erp.dto.inventorySuppliers.InventorySuppliersDto;
import com.erp.backend_erp.dto.inventorySuppliers.UpdateInventorySuppliersDto;
import com.erp.backend_erp.entity.inventory.InventorySuppliersEntity;

@Mapper(componentModel = "spring")
public interface InventorySuppliersMapper {
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "inventoryId", source = "dto.inventoryId"),
        @Mapping(target = "supplierId", source = "dto.supplierId"),
        @Mapping(target = "codigoProveedor", source = "dto.codigoProveedor"),
        @Mapping(target = "precioReferencia", source = "dto.precioReferencia"),
        @Mapping(target = "esPrincipal", source = "dto.esPrincipal"),
        @Mapping(target = "activo", source = "dto.activo")
    })
    InventorySuppliersEntity createToEntity(CreateInventorySuppliersDto dto);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "inventoryId", source = "dto.inventoryId"),
        @Mapping(target = "supplierId", source = "dto.supplierId"),
        @Mapping(target = "codigoProveedor", source = "dto.codigoProveedor"),
        @Mapping(target = "precioReferencia", source = "dto.precioReferencia"),
        @Mapping(target = "esPrincipal", source = "dto.esPrincipal"),
        @Mapping(target = "activo", source = "dto.activo")
    })
    void updateEntityFromDto(UpdateInventorySuppliersDto dto, @MappingTarget InventorySuppliersEntity entity);

    InventorySuppliersDto toDto(InventorySuppliersEntity entity);
}
