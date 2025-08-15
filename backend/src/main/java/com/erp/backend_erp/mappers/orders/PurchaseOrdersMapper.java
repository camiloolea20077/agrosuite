package com.erp.backend_erp.mappers.orders;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.purchaseOrders.CreatePurchaseOrdersDto;
import com.erp.backend_erp.dto.purchaseOrders.PurchaseOrdersDto;
import com.erp.backend_erp.dto.purchaseOrders.UpdatePurchaseOrdersDto;
import com.erp.backend_erp.entity.inventory.PurchaseOrdersEntity;

@Mapper(componentModel = "spring")
public interface PurchaseOrdersMapper {
@Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "numeroOrden", source = "dto.numeroOrden"),
        @Mapping(target = "supplierId", source = "dto.supplierId"),
        @Mapping(target = "farmId", source = "dto.farmId"),
        @Mapping(target = "insumoRequestId", source = "dto.insumoRequestId"),
        @Mapping(target = "fechaOrden", source = "dto.fechaOrden"),
        @Mapping(target = "fechaEntregaEsperada", source = "dto.fechaEntregaEsperada"),
        @Mapping(target = "fechaEntregaReal", source = "dto.fechaEntregaReal"),
        @Mapping(target = "subtotal", source = "dto.subtotal"),
        @Mapping(target = "impuestos", source = "dto.impuestos"),
        @Mapping(target = "total", source = "dto.total"),
        @Mapping(target = "observaciones", source = "dto.observaciones"),
        @Mapping(target = "estado", source = "dto.estado"),
        @Mapping(target = "createdBy", source = "dto.createdBy")
    })
    PurchaseOrdersEntity createToEntity(CreatePurchaseOrdersDto dto);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "numeroOrden", source = "dto.numeroOrden"),
        @Mapping(target = "supplierId", source = "dto.supplierId"),
        @Mapping(target = "farmId", source = "dto.farmId"),
        @Mapping(target = "insumoRequestId", source = "dto.insumoRequestId"),
        @Mapping(target = "fechaOrden", source = "dto.fechaOrden"),
        @Mapping(target = "fechaEntregaEsperada", source = "dto.fechaEntregaEsperada"),
        @Mapping(target = "fechaEntregaReal", source = "dto.fechaEntregaReal"),
        @Mapping(target = "subtotal", source = "dto.subtotal"),
        @Mapping(target = "impuestos", source = "dto.impuestos"),
        @Mapping(target = "total", source = "dto.total"),
        @Mapping(target = "observaciones", source = "dto.observaciones"),
        @Mapping(target = "estado", source = "dto.estado")
    })
    void updateEntityFromDto(UpdatePurchaseOrdersDto dto, @MappingTarget PurchaseOrdersEntity entity);

    PurchaseOrdersDto toDto(PurchaseOrdersEntity entity);
}
