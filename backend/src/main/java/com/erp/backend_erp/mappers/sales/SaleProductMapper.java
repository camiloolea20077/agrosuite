package com.erp.backend_erp.mappers.sales;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import com.erp.backend_erp.dto.sales.SaleProductDto;
import com.erp.backend_erp.entity.inventory.InventoryEntity;
import com.erp.backend_erp.entity.sales.SaleProductEntity;
@Mapper(componentModel = "spring")
public interface SaleProductMapper {

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "sale", ignore = true),
        @Mapping(target = "product", source = "productId", qualifiedByName = "mapProduct"),
        @Mapping(target = "quantity", source = "quantity"),
        @Mapping(target = "price", source = "price"),
        @Mapping(target = "unit_price", source = "unitPrice")
    })
    SaleProductEntity toEntity(SaleProductDto dto);

    @Mappings({
        @Mapping(target = "productId", source = "product.id"),
        @Mapping(target = "quantity", source = "quantity"),
        @Mapping(target = "unitPrice", source = "unit_price"),
        @Mapping(target = "price", source = "price")
    })
    SaleProductDto toDto(SaleProductEntity entity);

    @Named("mapProduct")
    default InventoryEntity mapProduct(Long id) {
        InventoryEntity product = new InventoryEntity();
        product.setId(id);
        return product;
    }
}
