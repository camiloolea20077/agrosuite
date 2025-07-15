package com.erp.backend_erp.services.implementations;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.listElements.CattleElementsDto;
import com.erp.backend_erp.repositories.ganado.GanadoQueryRepository;
import com.erp.backend_erp.services.ListElementService;
import com.erp.backend_erp.util.GlobalException;

@Service
public class ListElementServiceImpl implements ListElementService {
    
    private final GanadoQueryRepository ganadoQueryRepository;

    public ListElementServiceImpl(GanadoQueryRepository ganadoQueryRepository) {
        this.ganadoQueryRepository = ganadoQueryRepository;
    }
    @Override
	public List<CattleElementsDto> findListForId() {
		List<CattleElementsDto> elements = ganadoQueryRepository.findListForId();
		if (elements.isEmpty())
			throw new GlobalException(HttpStatus.OK, "No se encontraron registros");
		return elements;
	}
    
    @Override
	public List<CattleElementsDto> findListForIdFemale() {
		List<CattleElementsDto> elements = ganadoQueryRepository.findListForIdFemale();
		if (elements.isEmpty())
			throw new GlobalException(HttpStatus.OK, "No se encontraron registros");
		return elements;
	}
}
