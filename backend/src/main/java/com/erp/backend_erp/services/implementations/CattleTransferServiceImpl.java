package com.erp.backend_erp.services.implementations;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.backend_erp.dto.cattleTransfer.CattleTransferDto;
import com.erp.backend_erp.dto.cattleTransfer.CattleTransferTableDto;
import com.erp.backend_erp.dto.cattleTransfer.CreateCattleTransferDto;
import com.erp.backend_erp.dto.cattleTransfer.ViewCattleTransferDto;
import com.erp.backend_erp.entity.cattleTransfer.CattleTransferEntity;
import com.erp.backend_erp.entity.cattleTransfer.CattleTransferItemEntity;
import com.erp.backend_erp.mappers.cattleTransfers.CattleTransferMapper;
import com.erp.backend_erp.repositories.births.BirthsQueryRepository;
import com.erp.backend_erp.repositories.cattleTransfer.CattleTransferItemJPARepository;
import com.erp.backend_erp.repositories.cattleTransfer.CattleTransferJPARepository;
import com.erp.backend_erp.repositories.cattleTransfer.CattleTransferQueryRepository;
import com.erp.backend_erp.repositories.ganado.GanadoQueryRepository;
import com.erp.backend_erp.services.CattleTransferService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@Service
public class CattleTransferServiceImpl implements CattleTransferService{


    @Autowired
    private CattleTransferJPARepository cattleTransferJPARepository;

    @Autowired
    private CattleTransferItemJPARepository cattleTransferItemJPARepository;

    @Autowired
    private CattleTransferMapper cattleTransferMapper;

    @Autowired
    private GanadoQueryRepository cattleQueryRepository;

    @Autowired
    private BirthsQueryRepository birthsQueryRepository;

    @Autowired
    private CattleTransferQueryRepository cattleTransferQueryRepository;
    
    @Override
    @Transactional
    public CattleTransferDto createTransfer(CreateCattleTransferDto dto, Long userId, Long farmId) {
        // 1. Mapear el transfer principal
        CattleTransferEntity transfer = cattleTransferMapper.toEntity(dto);
        transfer.setCreatedAt(LocalDateTime.now());
        transfer.setCreatedBy(userId);
        transfer.setFarmId(farmId);

        // 2. Guardar el transfer
        CattleTransferEntity savedTransfer = cattleTransferJPARepository.save(transfer);

        // 3. Mapear los ítems
        List<CattleTransferItemEntity> items = cattleTransferMapper.toEntityItemList(dto.getItems());
        items.forEach(item -> {
            item.setTransferId(savedTransfer.getId());
            item.setCreated_at(LocalDateTime.now());
        });

        // 4. Guardar ítems
        cattleTransferItemJPARepository.saveAll(items);

        // 5. Actualizar farm_id en cattle o births según tipo
        if ("GANADO".equalsIgnoreCase(savedTransfer.getTransferType())) {
            items.stream()
                .filter(item -> item.getCattleId() != null)
                .forEach(item -> {
                    // Actualiza el farm_id del ganado
                    cattleQueryRepository.updateFarmId(item.getCattleId(), savedTransfer.getDestinationFarmId());
                });
        } else if ("TERNERO".equalsIgnoreCase(savedTransfer.getTransferType())) {
            items.stream()
                .filter(item -> item.getBirthId() != null)
                .forEach(item -> {
                    // Actualiza el farm_id del ternero
                    birthsQueryRepository.updateFarmId(item.getBirthId(), savedTransfer.getDestinationFarmId());
                });
        }
         // 6. Cargar ítems desde la base de datos
        List<CattleTransferItemEntity> savedItems = cattleTransferItemJPARepository.findAllByTransferId(savedTransfer.getId());

        // 7. Mapear a DTO
        CattleTransferDto resultDto = cattleTransferMapper.toDto(savedTransfer);
        resultDto.setItems(cattleTransferMapper.toItemDtoList(savedItems));

        return resultDto;
    }

    @Override
    public PageImpl<CattleTransferTableDto> pageTransfers(PageableDto<Object> pageableDto) {
        return cattleTransferQueryRepository.pageTransfers(pageableDto);
    }
    @Override
    @Transactional(readOnly = true)
    public ViewCattleTransferDto getTransferById(Long id, Long farmId) {
        CattleTransferEntity entity = cattleTransferJPARepository.findByIdAndFarmId(id, farmId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Transferencia no encontrada"));
        List<CattleTransferItemEntity> items = cattleTransferItemJPARepository.findAllByTransferId(entity.getId());
        return cattleTransferMapper.toViewDto(entity, items);
    }
}