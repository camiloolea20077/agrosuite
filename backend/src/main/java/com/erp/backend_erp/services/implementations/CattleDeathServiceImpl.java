package com.erp.backend_erp.services.implementations;

import java.util.Arrays;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.backend_erp.dto.cattleDeath.CattleDeathDto;
import com.erp.backend_erp.dto.cattleDeath.CreateCattleDeathDto;
import com.erp.backend_erp.dto.cattleDeath.DeathsTableDto;
import com.erp.backend_erp.dto.cattleDeath.ViewCattleDeathDto;
import com.erp.backend_erp.entity.deathCattle.CattleDeathEntity;
import com.erp.backend_erp.mappers.cattleDeath.CattleDeathMapper;
import com.erp.backend_erp.repositories.cattleDeath.CattleDeathJPARepository;
import com.erp.backend_erp.repositories.cattleDeath.CattleDeathQueryRepository;
import com.erp.backend_erp.services.CattleDeathService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@Service
public class CattleDeathServiceImpl implements CattleDeathService{
    

    private final CattleDeathJPARepository cattleDeathRepository;

    private final CattleDeathMapper cattleDeathMapper;

    private final CattleDeathQueryRepository cattleDeathQueryRepository;

    public CattleDeathServiceImpl(CattleDeathJPARepository cattleDeathRepository, CattleDeathMapper cattleDeathMapper, CattleDeathQueryRepository cattleDeathQueryRepository) {
        this.cattleDeathRepository = cattleDeathRepository;
        this.cattleDeathMapper = cattleDeathMapper;
        this.cattleDeathQueryRepository = cattleDeathQueryRepository;
    }
    @Override
    @Transactional
    public CattleDeathDto create(CreateCattleDeathDto dto) {
            // Validar que solo uno de los dos IDs esté presente
            if ((dto.getCattleId() == null && dto.getBirthId() == null) || 
                (dto.getCattleId() != null && dto.getBirthId() != null)) {
                throw new GlobalException(
                    HttpStatus.BAD_REQUEST, 
                    "Debe especificar solo uno: cattle_id o birth_id"
                );
            }

            // Validar existencia del animal
            if (dto.getCattleId() != null) {
                boolean exists = cattleDeathQueryRepository.existsGanadoById(dto.getCattleId(), dto.getFarmId());
                if (!exists) {
                    throw new GlobalException(
                        HttpStatus.NOT_FOUND,
                        "El ganado con ID " + dto.getCattleId() + " no existe o no pertenece a la finca"
                    );
                }
                
                // Validar que no esté ya registrado como muerto
                boolean alreadyDead = cattleDeathRepository.existsByCattleIdAndFarmId(dto.getCattleId(), dto.getFarmId());
                if (alreadyDead) {
                    throw new GlobalException(
                        HttpStatus.BAD_REQUEST,
                        "Este ganado ya está registrado como muerto"
                    );
                }
            }

            if (dto.getBirthId() != null) {
                boolean exists = cattleDeathQueryRepository.existsBirthById(dto.getBirthId(), dto.getFarmId());
                if (!exists) {
                    throw new GlobalException(
                        HttpStatus.NOT_FOUND,
                        "El ternero con ID " + dto.getBirthId() + " no existe o no pertenece a la finca"
                    );
                }
                
                // Validar que no esté ya registrado como muerto
                boolean alreadyDead = cattleDeathRepository.existsByBirthIdAndFarmId(dto.getBirthId(), dto.getFarmId());
                if (alreadyDead) {
                    throw new GlobalException(
                        HttpStatus.BAD_REQUEST,
                        "Este ternero ya está registrado como muerto"
                    );
                }
            }

            // Crear la entidad de muerte
            CattleDeathEntity deathEntity = cattleDeathMapper.toEntity(dto);
            
            // Guardar el registro de muerte
            CattleDeathEntity saved = cattleDeathRepository.save(deathEntity);

            // Marcar el animal como muerto/inactivo
            if (dto.getCattleId() != null) {
                cattleDeathQueryRepository.marcarGanadoComoMuerto(Arrays.asList(dto.getCattleId()));
            } else if (dto.getBirthId() != null) {
                cattleDeathQueryRepository.marcarNacimientosComoMuertos(Arrays.asList(dto.getBirthId()));
            }

            return cattleDeathMapper.toDto(saved);
    }
    @Override
    public PageImpl<DeathsTableDto> pageDeaths(PageableDto<Object> pageableDto) {
        return cattleDeathQueryRepository.listDeaths(pageableDto);
    }
    @Override
    @Transactional(readOnly = true)
    public ViewCattleDeathDto findById(Integer id, Long farmId) {
        return cattleDeathQueryRepository.findDetailedById(id, farmId)
            .orElseThrow(() -> new GlobalException(
                HttpStatus.NOT_FOUND,
                "Registro de muerte no encontrado"
        ));
    }
    @Override
	public CattleDeathDto findByIds(Integer id, Long farmId) {
		CattleDeathEntity employeesEntity =cattleDeathRepository.findByIdAndFarmId(id, farmId)
				.orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
		CattleDeathDto employeesDto = cattleDeathMapper.toDto(employeesEntity);
		return employeesDto;
	}
}
