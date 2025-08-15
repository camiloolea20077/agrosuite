package com.erp.backend_erp.mappers.tiposMovimientos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.InventoryMovements.CreateInventoryMovementsDto;
import com.erp.backend_erp.dto.InventoryMovements.InventoryMovementsDto;
import com.erp.backend_erp.dto.InventoryMovements.UpdateInventoryMovementsDto;
import com.erp.backend_erp.entity.inventory.InventoryMovementsEntity;

@Mapper(componentModel = "spring")
public interface  InventoryMovementsMapper {
@Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deletedAt", ignore = true),
        @Mapping(target = "updatedBy", ignore = true),
        @Mapping(target = "inventoryId", source = "dto.inventoryId"),
        @Mapping(target = "farmId", source = "dto.farmId"),
        @Mapping(target = "tipoMovimientoId", source = "dto.tipoMovimientoId"),
        @Mapping(target = "estadoId", source = "dto.estadoId"),
        @Mapping(target = "cantidad", source = "dto.cantidad"),
        @Mapping(target = "unidadMedida", source = "dto.unidadMedida"),
        @Mapping(target = "cantidadAnterior", source = "dto.cantidadAnterior"),
        @Mapping(target = "cantidadNueva", source = "dto.cantidadNueva"),
        @Mapping(target = "fechaMovimiento", source = "dto.fechaMovimiento"),
        @Mapping(target = "fechaProgramada", source = "dto.fechaProgramada"),
        @Mapping(target = "employeeId", source = "dto.employeeId"),
        @Mapping(target = "numeroDocumento", source = "dto.numeroDocumento"),
        @Mapping(target = "observaciones", source = "dto.observaciones"),
        @Mapping(target = "notas", source = "dto.notas"),
        @Mapping(target = "cantidadDevuelta", source = "dto.cantidadDevuelta"),
        @Mapping(target = "cantidadUsada", source = "dto.cantidadUsada"),
        @Mapping(target = "fechaDevolucion", source = "dto.fechaDevolucion"),
        @Mapping(target = "estaCerrado", source = "dto.estaCerrado"),
        @Mapping(target = "ubicacionOrigen", source = "dto.ubicacionOrigen"),
        @Mapping(target = "ubicacionDestino", source = "dto.ubicacionDestino"),
        @Mapping(target = "requiereAprobacion", source = "dto.requiereAprobacion"),
        @Mapping(target = "aprobadoPor", source = "dto.aprobadoPor"),
        @Mapping(target = "fechaAprobacion", source = "dto.fechaAprobacion"),
        @Mapping(target = "esAutomatico", source = "dto.esAutomatico"),
        @Mapping(target = "movimientoPadreId", source = "dto.movimientoPadreId"),
        @Mapping(target = "createdBy", source = "dto.createdBy")
    })
    InventoryMovementsEntity createToEntity(CreateInventoryMovementsDto dto);

    @Mappings({
        @Mapping(target = "id", source = "dto.id"),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deletedAt", ignore = true),
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "inventoryId", source = "dto.inventoryId"),
        @Mapping(target = "farmId", source = "dto.farmId"),
        @Mapping(target = "tipoMovimientoId", source = "dto.tipoMovimientoId"),
        @Mapping(target = "estadoId", source = "dto.estadoId"),
        @Mapping(target = "cantidad", source = "dto.cantidad"),
        @Mapping(target = "unidadMedida", source = "dto.unidadMedida"),
        @Mapping(target = "cantidadAnterior", source = "dto.cantidadAnterior"),
        @Mapping(target = "cantidadNueva", source = "dto.cantidadNueva"),
        @Mapping(target = "fechaMovimiento", source = "dto.fechaMovimiento"),
        @Mapping(target = "fechaProgramada", source = "dto.fechaProgramada"),
        @Mapping(target = "employeeId", source = "dto.employeeId"),
        @Mapping(target = "numeroDocumento", source = "dto.numeroDocumento"),
        @Mapping(target = "observaciones", source = "dto.observaciones"),
        @Mapping(target = "notas", source = "dto.notas"),
        @Mapping(target = "cantidadDevuelta", source = "dto.cantidadDevuelta"),
        @Mapping(target = "cantidadUsada", source = "dto.cantidadUsada"),
        @Mapping(target = "fechaDevolucion", source = "dto.fechaDevolucion"),
        @Mapping(target = "estaCerrado", source = "dto.estaCerrado"),
        @Mapping(target = "ubicacionOrigen", source = "dto.ubicacionOrigen"),
        @Mapping(target = "ubicacionDestino", source = "dto.ubicacionDestino"),
        @Mapping(target = "requiereAprobacion", source = "dto.requiereAprobacion"),
        @Mapping(target = "aprobadoPor", source = "dto.aprobadoPor"),
        @Mapping(target = "fechaAprobacion", source = "dto.fechaAprobacion"),
        @Mapping(target = "esAutomatico", source = "dto.esAutomatico"),
        @Mapping(target = "movimientoPadreId", source = "dto.movimientoPadreId"),
        @Mapping(target = "updatedBy", source = "dto.updatedBy")
    })
    void updateEntityFromDto(UpdateInventoryMovementsDto dto, @MappingTarget InventoryMovementsEntity entity);

    InventoryMovementsDto toDto(InventoryMovementsEntity entity);
}
