package com.erp.backend_erp.mappers.orders;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.purchaseOrders.CreatePurchaseOrderDetailsDto;
import com.erp.backend_erp.dto.purchaseOrders.PurchaseOrderDetailsDto;
import com.erp.backend_erp.dto.purchaseOrders.UpdatePurchaseOrderDetailsDto;
import com.erp.backend_erp.entity.inventory.PurchaseOrderDetailsEntity;

@Mapper(componentModel = "spring")
public interface PurchaseOrderDetailsMapper {
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "purchaseOrderId", source = "dto.purchaseOrderId"),
        @Mapping(target = "inventoryId", source = "dto.inventoryId"),
        @Mapping(target = "cantidadOrdenada", source = "dto.cantidadOrdenada"),
        @Mapping(target = "cantidadRecibida", source = "dto.cantidadRecibida"),
        @Mapping(target = "unidadMedida", source = "dto.unidadMedida"),
        @Mapping(target = "precioUnitario", source = "dto.precioUnitario"),
        @Mapping(target = "subtotal", source = "dto.subtotal"),
        @Mapping(target = "observaciones", source = "dto.observaciones")
    })
    PurchaseOrderDetailsEntity createToEntity(CreatePurchaseOrderDetailsDto dto);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "purchaseOrderId", source = "dto.purchaseOrderId"),
        @Mapping(target = "inventoryId", source = "dto.inventoryId"),
        @Mapping(target = "cantidadOrdenada", source = "dto.cantidadOrdenada"),
        @Mapping(target = "cantidadRecibida", source = "dto.cantidadRecibida"),
        @Mapping(target = "unidadMedida", source = "dto.unidadMedida"),
        @Mapping(target = "precioUnitario", source = "dto.precioUnitario"),
        @Mapping(target = "subtotal", source = "dto.subtotal"),
        @Mapping(target = "observaciones", source = "dto.observaciones")
    })
    void updateEntityFromDto(UpdatePurchaseOrderDetailsDto dto, @MappingTarget PurchaseOrderDetailsEntity entity);

    PurchaseOrderDetailsDto toDto(PurchaseOrderDetailsEntity entity);
}
