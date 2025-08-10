package com.erp.backend_erp.mappers.cattleTransfers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.erp.backend_erp.dto.cattleTransfer.CattleTransferDto;
import com.erp.backend_erp.dto.cattleTransfer.CattleTransferItemDto;
import com.erp.backend_erp.dto.cattleTransfer.CreateCattleTransferDto;
import com.erp.backend_erp.dto.cattleTransfer.CreateCattleTransferItemDto;
import com.erp.backend_erp.entity.cattleTransfer.CattleTransferEntity;
import com.erp.backend_erp.entity.cattleTransfer.CattleTransferItemEntity;
@Mapper(componentModel = "spring")
public interface  CattleTransferMapper {
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "createdBy", ignore = true),
        @Mapping(target = "farmId", ignore = true),  
        @Mapping(target = "createdAt", ignore = true),
        @Mapping(target = "transferDate", source="transferDate"),

    })
    CattleTransferEntity toEntity(CreateCattleTransferDto dto);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "transferId", ignore = true),
        @Mapping(target = "cattleId", source = "cattleId"),
        @Mapping(target = "birthId", source = "birthId"),
        @Mapping(target = "created_at", ignore = true)
    })
    CattleTransferItemEntity toEntityItem(CreateCattleTransferItemDto dto);

    List<CattleTransferItemEntity> toEntityItemList(List<CreateCattleTransferItemDto> dtoList);

    // DTOs
    CattleTransferDto toDto(CattleTransferEntity entity);
    List<CattleTransferDto> toDtoList(List<CattleTransferEntity> entityList);

    CattleTransferItemDto toItemDto(CattleTransferItemEntity entity);
    List<CattleTransferItemDto> toItemDtoList(List<CattleTransferItemEntity> entityList);
}
