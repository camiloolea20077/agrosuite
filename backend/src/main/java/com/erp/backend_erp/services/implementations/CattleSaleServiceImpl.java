package com.erp.backend_erp.services.implementations;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.backend_erp.dto.cattleSales.CattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.CreateCattleSaleDto;
import com.erp.backend_erp.dto.cattleSales.CreateCattleSaleItemDto;
import com.erp.backend_erp.dto.cattleSales.SalesTableDto;
import com.erp.backend_erp.dto.cattleSales.ViewCattleSaleDto;
import com.erp.backend_erp.entity.cattleSales.CattleSaleEntity;
import com.erp.backend_erp.entity.cattleSales.CattleSaleItemEntity;
import com.erp.backend_erp.mappers.cattleSales.CattleSaleMapper;
import com.erp.backend_erp.repositories.births.BirthsQueryRepository;
import com.erp.backend_erp.repositories.cattle_sales.CattleSalesJPARepository;
import com.erp.backend_erp.repositories.cattle_sales.CattleSalesQueryRepository;
import com.erp.backend_erp.repositories.ganado.GanadoQueryRepository;
import com.erp.backend_erp.services.CattleSaleService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;
@Service
public class CattleSaleServiceImpl implements CattleSaleService{

    @Autowired
    private CattleSalesJPARepository salesRepository;

    @Autowired
    private CattleSalesQueryRepository salesQueryRepository;

    @Autowired
    private CattleSaleMapper mapper;

    @Autowired
    private GanadoQueryRepository ganadoQueryRepository;

    @Autowired
    private BirthsQueryRepository birthsQueryRepository;


    @Override
    @Transactional
    public CattleSaleDto create(CreateCattleSaleDto dto) {
        // 1. Validar existencia de cada ganado/ternero (origen)
        for (CreateCattleSaleItemDto item : dto.getItems()) {
            boolean exists = salesQueryRepository.existsByOrigen(
                item.getTipoOrigen(),
                item.getIdOrigen(),
                dto.getFarmId()
            );

            if (!exists) {
                throw new RuntimeException("El " + item.getTipoOrigen().toLowerCase()
                    + " con ID " + item.getIdOrigen()
                    + " no existe o no pertenece a la finca");
            }
        }

        // 2. Mapear entidad principal
        CattleSaleEntity saleEntity = mapper.toEntity(dto);

        // 3. Mapear ítems con referencia a la entidad
        List<CattleSaleItemEntity> items = dto.getItems().stream().map(itemDto -> {
            CattleSaleItemEntity itemEntity = mapper.toEntityItem(itemDto);
            itemEntity.setSale(saleEntity); // ← relación ManyToOne
            itemEntity.setFarmId(dto.getFarmId()); // Asignar farmId a cada item
            return itemEntity;
        }).collect(Collectors.toList());

        saleEntity.setItems(items); // ← ahora sí, antes de guardar

        // 4. Guardar venta con sus items
        CattleSaleEntity saved = salesRepository.save(saleEntity);

        // 5. Marcar como vendidos los animales
        List<Long> idsGanado = dto.getItems().stream()
            .filter(i -> i.getTipoOrigen().equalsIgnoreCase("GANADO"))
            .map(CreateCattleSaleItemDto::getIdOrigen)
            .toList();
        List<Long> idsNacimientos = dto.getItems().stream()
            .filter(i -> i.getTipoOrigen().equalsIgnoreCase("NACIMIENTO"))
            .map(CreateCattleSaleItemDto::getIdOrigen)
            .toList();

        if (!idsGanado.isEmpty()) {
            ganadoQueryRepository.marcarGanadoComoVendido(idsGanado);
        }
        if (!idsNacimientos.isEmpty()) {
            birthsQueryRepository.marcarNacimientosComoVendidos(idsNacimientos);
}

        return mapper.toDto(saved);
    }

    @Override
    public PageImpl<SalesTableDto> pageSales(PageableDto<Object> pageableDto) {
        return salesQueryRepository.listSales(pageableDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ViewCattleSaleDto findById(Long id, Long farmId) {
        CattleSaleEntity entity = salesRepository.findByIdAndFarmId(id, farmId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        List<CattleSaleItemEntity> items = salesQueryRepository.findItemsBySaleId(entity.getId());

        return mapper.toViewDto(entity, items);
    }

}
