package com.erp.backend_erp.mappers.cattleSales;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.cattleSales.CattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.CreateCattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.CreateCattleSaleItemDto;
import com.erp.backend_erp.dto.cattleSales.ViewCattleSaleDto;
import com.erp.backend_erp.entity.cattleSales.CattleSaleEntity;
import com.erp.backend_erp.entity.cattleSales.CattleSaleItemEntity;

@Mapper(componentModel = "spring")
public interface CattleSaleMapper {

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "tipoVenta", source = "tipoVenta"),
        @Mapping(target = "fechaVenta", source = "fechaVenta"),
        @Mapping(target = "horaEmision", source = "horaEmision"),
        @Mapping(target = "numeroFactura", source = "numeroFactura"),
        @Mapping(target = "precioKilo", source = "precioKilo"),
        @Mapping(target = "pesoTotal", source = "pesoTotal"),
        @Mapping(target = "subtotal", source = "subtotal"),
        @Mapping(target = "iva", source = "iva"),
        @Mapping(target = "descuentos", source = "descuentos"),
        @Mapping(target = "total", source = "total"),
        @Mapping(target = "moneda", source = "moneda"),
        @Mapping(target = "formaPago", source = "formaPago"),
        @Mapping(target = "destino", source = "destino"),
        @Mapping(target = "observaciones", source = "observaciones"),
        @Mapping(target = "farmId", source = "farmId"),
        @Mapping(target = "terceroId", source = "terceroId"),
        @Mapping(target = "cattleIds", source = "cattleIds"),
        @Mapping(target = "items", ignore = true)
    })
    CattleSaleEntity toEntity(CreateCattleSaleDto dto);

    CattleSaleDto toDto(CattleSaleEntity entity);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "sale", ignore = true),
        @Mapping(target = "tipoOrigen", source = "tipoOrigen"),
        @Mapping(target = "idOrigen", source = "idOrigen"),
        @Mapping(target = "pesoVenta", source = "pesoVenta"),
        @Mapping(target = "precioKilo", source = "precioKilo"),
        @Mapping(target = "precioTotal", source = "precioTotal"),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true)
    })
    CattleSaleItemEntity toEntityItem(CreateCattleSaleItemDto dto);

    @Mappings({
        @Mapping(target = "items", source = "items")
    })
    ViewCattleSaleDto toViewDto(CattleSaleEntity entity, List<CattleSaleItemEntity> items);

    CreateCattleSaleItemDto toViewItemDto(CattleSaleItemEntity entity);
    List<CreateCattleSaleItemDto> toViewItemDtoList(List<CattleSaleItemEntity> items);
}

