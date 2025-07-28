package com.erp.backend_erp.services.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.ganado.CreateGanadoDto;
import com.erp.backend_erp.dto.ganado.GanadoDto;
import com.erp.backend_erp.dto.ganado.GanadoTableDto;
import com.erp.backend_erp.dto.ganado.UpdateGanadoDto;
import com.erp.backend_erp.entity.ganado.GanadoEntity;
import com.erp.backend_erp.mappers.ganado.GanadoMapper;
import com.erp.backend_erp.repositories.ganado.GanadoJPARepository;
import com.erp.backend_erp.repositories.ganado.GanadoQueryRepository;
import com.erp.backend_erp.services.GanadoService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@Service
public class GanadoServiceImpl implements GanadoService {

    private final GanadoQueryRepository ganadoQueryRepository;
    private final GanadoJPARepository ganadoJPARepository;
    private final GanadoMapper ganadoMapper;

    public GanadoServiceImpl(GanadoQueryRepository ganadoQueryRepository, GanadoJPARepository ganadoJPARepository, GanadoMapper ganadoMapper) {
        this.ganadoQueryRepository = ganadoQueryRepository;
        this.ganadoJPARepository = ganadoJPARepository;
        this.ganadoMapper = ganadoMapper;
    }

    @Override
    public GanadoDto create(CreateGanadoDto createGanadoDto) {
        Boolean exists = ganadoQueryRepository.existCattle(createGanadoDto.getNumero_ganado().toLowerCase());
        if (exists)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El Numero de ganado ya se encuentra registrado");
        try{
            GanadoEntity ganadoEntity = ganadoMapper.createToEntity(createGanadoDto);
            ganadoEntity.setFarmId(createGanadoDto.getFarmId());
            GanadoEntity saveGanadoEntity = ganadoJPARepository.save(ganadoEntity);
            return ganadoMapper.toDto(saveGanadoEntity);
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @Override
    public List<GanadoDto> createAll(List<CreateGanadoDto> dtoList) {
        List<GanadoDto> result = new ArrayList<>();

        for (CreateGanadoDto dto : dtoList) {
            try {
                GanadoDto saved = this.create(dto); // Usa el método individual
                result.add(saved);
            } catch (Exception e) {
                // Si quieres ignorar duplicados y continuar con el resto:
                // Logueas y sigues con los demás
                System.out.println("No se pudo guardar GND " + dto.getNumero_ganado() + ": " + e.getMessage());
            }
        }

        return result;
    }

    @Override
    public PageImpl<GanadoTableDto> pageGanado(PageableDto<Object> pageableDto) {
        return ganadoQueryRepository.listCattle(pageableDto);
    }
    
    @Override
    public Boolean update(UpdateGanadoDto updateGanadoDto) {
        GanadoEntity ganadoEntity = ganadoJPARepository.findById(updateGanadoDto.getId())
                    .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        try {
            ganadoMapper.updateEntityFromDto(updateGanadoDto, ganadoEntity);
            ganadoJPARepository.save(ganadoEntity);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el registro");
        }
    }
    @Override
	public Boolean delete(Long id) {
		ganadoJPARepository.findById(id)
				.orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
                ganadoJPARepository.deleteById(id);
		return true;
	}
    @Override
	public GanadoDto findById(Long id , Long farmId) {
		GanadoEntity ganadoEntity =ganadoJPARepository.findByIdAndFarmId(id , farmId)
				.orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
		GanadoDto ganadoDto = ganadoMapper.toDto(ganadoEntity);
		return ganadoDto;
	}

}
