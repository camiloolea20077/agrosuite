package com.erp.backend_erp.mappers.cattleSales;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.cattleSales.CattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.CreateCattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.CreateCattleSaleItemDto;
import com.erp.backend_erp.entity.cattleSales.CattleSaleEntity;
import com.erp.backend_erp.entity.cattleSales.CattleSaleItemEntity;

@Mapper(componentModel = "spring")
public interface  CattleSaleMapper {
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "fechaVenta", source = "dto.fechaVenta"),
        @Mapping(target = "pesoTotal", source = "dto.pesoTotal"),
        @Mapping(target = "precioKilo", source = "dto.precioKilo"),
        @Mapping(target = "precioTotal", source = "precioTotal"),
        @Mapping(target = "destino", source = "dto.destino"),
        @Mapping(target = "farmId", source = "dto.farmId"),
        @Mapping(target = "comprador", source = "dto.comprador"),
        @Mapping(target = "cattleIds", source = "dto.cattleIds"),
        @Mapping(target = "observaciones", source = "dto.observaciones"),
        @Mapping(target = "tipoVenta", source = "dto.tipoVenta"),
    })
    CattleSaleEntity toEntity(CreateCattleSaleDto dto);

    CattleSaleDto toDto(CattleSaleEntity entity);
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "sale", ignore = true),
        @Mapping(target = "tipoOrigen", source = "tipoOrigen"),
        @Mapping(target = "idOrigen", source = "idOrigen"),
        @Mapping(target = "pesoVenta", source = "pesoVenta"),
        // @Mapping(target = "precioKilo", ignore = true),   
        // @Mapping(target = "precioTotal", ignore = true),  
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true)
    })
    CattleSaleItemEntity toEntityItem(CreateCattleSaleItemDto dto);
}
