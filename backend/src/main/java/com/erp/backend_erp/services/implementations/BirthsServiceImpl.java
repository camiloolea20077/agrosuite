package com.erp.backend_erp.services.implementations;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.births.BirthsDto;
import com.erp.backend_erp.dto.births.BirthsTableDto;
import com.erp.backend_erp.dto.births.CreateBirthsDto;
import com.erp.backend_erp.dto.births.DashboardBirthDto;
import com.erp.backend_erp.dto.births.DashboardData;
import com.erp.backend_erp.dto.births.UpdateBirthsDto;
import com.erp.backend_erp.entity.births.BirthsEntity;
import com.erp.backend_erp.mappers.births.BirthsMapper;
import com.erp.backend_erp.repositories.births.BirthsJPARepository;
import com.erp.backend_erp.repositories.births.BirthsQueryRepository;
import com.erp.backend_erp.services.BirthsService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@Service
public class BirthsServiceImpl  implements BirthsService {
    
    private final BirthsJPARepository birthsJPARepository;
    private final BirthsQueryRepository birthsQueryRepository;
    private final BirthsMapper birthsMapper;

    public BirthsServiceImpl(BirthsJPARepository birthsJPARepository, BirthsQueryRepository birthsQueryRepository, BirthsMapper birthsMapper) {
        this.birthsJPARepository = birthsJPARepository;
        this.birthsQueryRepository = birthsQueryRepository;
        this.birthsMapper = birthsMapper;
    }

    @Override
    public BirthsDto create(CreateBirthsDto createBirthsDto) {
        Boolean exists = birthsQueryRepository.existCattle(createBirthsDto.getNumero_cria().toLowerCase());
        if (exists)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El Numero de cria ya se encuentra registrado");
        try{
            BirthsEntity ganadoEntity = birthsMapper.createToEntity(createBirthsDto);
            ganadoEntity.setFarmId(createBirthsDto.getFarmId());
            BirthsEntity saveGanadoEntity = birthsJPARepository.save(ganadoEntity);
            return birthsMapper.toDto(saveGanadoEntity);
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @Override
    public Boolean update(UpdateBirthsDto updateBirthsDto) {
        BirthsEntity ganadoEntity = birthsJPARepository.findById(updateBirthsDto.getId())
                    .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        try {
            birthsMapper.updateEntityFromDto(updateBirthsDto, ganadoEntity);
            birthsJPARepository.save(ganadoEntity);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el registro");
        }
    }
    @Override
    public Boolean delete(Long id) {
        birthsJPARepository.findById(id)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        birthsJPARepository.deleteById(id);
        return true;
    }
    @Override
    public BirthsDto findById(Long id, Long farmId) {
        BirthsEntity entity = birthsJPARepository.findByIdAndFarmId(id, farmId)
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        return birthsMapper.toDto(entity);
    }
    
    @Override
    public PageImpl<BirthsTableDto> pageGanado(PageableDto<Object> pageableDto) {
        return birthsQueryRepository.listBirths(pageableDto);
    }

    @Override
    public DashboardData getDashboardData() {
        // Obtener los datos de nacimientos por mes (machos y hembras)
        List<DashboardBirthDto> birthData = birthsQueryRepository.getBirthsByMonth();

        // Obtener el total de ganado
        long totalCattle = birthsQueryRepository.getTotalCattleCount();
        Long totalBirths = birthsQueryRepository.getTotalBirths();
        Long totalEmployees = birthsQueryRepository.getTotalEmployees();
        // Crear el objeto DashboardData con los datos obtenidos
        return new DashboardData(birthData, totalCattle, totalBirths, totalEmployees);
    }
}
