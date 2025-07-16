package com.erp.backend_erp.mappers.sales;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.sales.CreateSaleDto;
import com.erp.backend_erp.dto.sales.SaleDto;
import com.erp.backend_erp.entity.sales.SaleEntity;

@Mapper(componentModel = "spring", uses = { SaleProductMapper.class })
public interface SalesMapper {

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "companyId", ignore = true),
        @Mapping(target = "created_at", ignore = true),
        @Mapping(target = "updated_at", ignore = true),
        @Mapping(target = "deleted_at", ignore = true),
        @Mapping(target = "total", ignore = true),
        @Mapping(target = "status", constant = "draft"),
        @Mapping(target = "products", source = "products")
    })
    SaleEntity createToEntity(CreateSaleDto dto);

    @Mappings({
        @Mapping(target = "id", source = "entity.id"),
        @Mapping(target = "customerId", source = "entity.customerId"),
        @Mapping(target = "total", source = "entity.total"),
        @Mapping(target = "status", source = "entity.status"),
        @Mapping(target = "products", source = "entity.products")
    })
    SaleDto toDto(SaleEntity entity);

    void updateEntityFromDto(CreateSaleDto dto, @MappingTarget SaleEntity entity);

    List<SaleDto> toDtoList(List<SaleEntity> entities);
}