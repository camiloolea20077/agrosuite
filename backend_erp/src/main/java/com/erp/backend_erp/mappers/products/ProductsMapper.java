package com.erp.backend_erp.mappers.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.products.CreateProductDto;
import com.erp.backend_erp.dto.products.ProductDto;
import com.erp.backend_erp.dto.products.UpdateProductDto;
import com.erp.backend_erp.entity.produts.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductsMapper {
        @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "created_at", ignore = true),
            @Mapping(target = "updated_at", ignore = true),
            @Mapping(target = "name", source = "dto.name"),
            @Mapping(target = "sku", source = "dto.sku"),
            @Mapping(target = "price", source = "dto.price"),
            @Mapping(target = "quantity", source = "dto.quantity"),
            @Mapping(target = "company_id", source = "dto.companyId"),
            @Mapping(target = "code", source = "dto.code"),
            @Mapping(target = "description", source = "dto.description"),
            @Mapping(target = "entry_date", source = "dto.entry_date"),
            @Mapping(target = "supplier_id", source = "dto.supplier_id"),
    })
    ProductEntity createToEntity(CreateProductDto dto);
    @Mappings({ @Mapping(target = "id", source = "dto.id"), @Mapping(target = "created_at", ignore = true),
			@Mapping(target = "updated_at", ignore = true), @Mapping(target = "deleted_at", ignore = true),
			@Mapping(target = "name", source = "dto.name"),
			@Mapping(target = "sku", source = "dto.sku"),
            @Mapping(target = "price", source = "dto.price"),
            @Mapping(target = "quantity", source = "dto.quantity"),
			})
	void updateEntityFromDto(UpdateProductDto dto, @MappingTarget ProductEntity entity);

    @Mappings({
            @Mapping(target = "id", source = "entity.id"),
            @Mapping(target = "name", source = "entity.name"),
            @Mapping(target = "sku", source = "entity.sku"),
            @Mapping(target = "price", source = "entity.price"),
            @Mapping(target = "companyId", source = "entity.company_id"),
            @Mapping(target = "code", source = "entity.code"),
            @Mapping(target = "description", source = "entity.description"),
            @Mapping(target = "quantity", source = "entity.quantity"),
            @Mapping(target = "entry_date", source = "entity.entry_date"),
            @Mapping(target = "supplier_id", source = "entity.supplier_id"),
    })
    ProductDto toDto(ProductEntity entity);
}
