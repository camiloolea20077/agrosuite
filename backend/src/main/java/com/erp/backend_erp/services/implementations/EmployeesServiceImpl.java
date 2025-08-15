package com.erp.backend_erp.services.implementations;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.employees.CreateEmployeesDto;
import com.erp.backend_erp.dto.employees.EmployeesDto;
import com.erp.backend_erp.dto.employees.EmployeesListDto;
import com.erp.backend_erp.dto.employees.EmployeesTableDto;
import com.erp.backend_erp.dto.employees.UpdateEmployeesDto;
import com.erp.backend_erp.entity.employees.EmployeesEntity;
import com.erp.backend_erp.mappers.employees.EmployeesMapper;
import com.erp.backend_erp.repositories.employees.EmployeesJPARepository;
import com.erp.backend_erp.repositories.employees.EmployeesQueryRepository;
import com.erp.backend_erp.services.EmployeesService;
import com.erp.backend_erp.util.GlobalException;
import com.erp.backend_erp.util.PageableDto;

@Service
public class EmployeesServiceImpl implements EmployeesService {


    private final EmployeesJPARepository employeesJPARepository;
    private final EmployeesQueryRepository employeesQueryRepository;
    private final EmployeesMapper employeesMapper;


    public EmployeesServiceImpl(EmployeesJPARepository employeesJPARepository, EmployeesQueryRepository employeesQueryRepository, EmployeesMapper employeesMapper) {
        this.employeesJPARepository = employeesJPARepository;
        this.employeesQueryRepository = employeesQueryRepository;
        this.employeesMapper = employeesMapper;
    }

    @Override
    public EmployeesDto create(CreateEmployeesDto createEmployeesDto) {
        Boolean exists = employeesQueryRepository.existsByDocument(createEmployeesDto.getIdentificacion().toLowerCase());
        if (exists)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El Numero de identificacion ya se encuentra registrado");
        try{
            EmployeesEntity ganadoEntity = employeesMapper.createToEntity(createEmployeesDto);
            ganadoEntity.setFarmId(createEmployeesDto.getFarmId());
            EmployeesEntity saveGanadoEntity = employeesJPARepository.save(ganadoEntity);
            return employeesMapper.toDto(saveGanadoEntity);
        } catch (Exception e) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    /**
     * Actualiza un registro de empleado en la base de datos.
     *
     * @param updateEmployeesDto objeto que contiene los datos del empleado a actualizar
     * @return true si el registro es actualizado correctamente, false en caso contrario
     * @throws GlobalException si el registro no existe o si ocurre un error inesperado
     */
    @Override
    public Boolean update(UpdateEmployeesDto updateEmployeesDto) {
        EmployeesEntity employeesEntity = employeesJPARepository.findById(updateEmployeesDto.getId())
                    .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        try {
            employeesMapper.updateEntityFromDto(updateEmployeesDto, employeesEntity);
            employeesJPARepository.save(employeesEntity);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el registro");
        }
    }
    @Override
	public Boolean delete(Long id) {
		employeesJPARepository.findById(id)
				.orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
                employeesJPARepository.deleteById(id);
		return true;
	}
    @Override
	public EmployeesDto findById(Long id, Long farmId) {
		EmployeesEntity employeesEntity =employeesJPARepository.findByIdAndFarmId(id, farmId)
				.orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
		EmployeesDto employeesDto = employeesMapper.toDto(employeesEntity);
		return employeesDto;
	}

    @Override
    public PageImpl<EmployeesTableDto> pageEmployees(PageableDto<Object> pageableDto) {
        return employeesQueryRepository.pageEmployees(pageableDto);
    }
    
    @Override
    public List<EmployeesListDto> getEmployees(Long farmId) {
        List<EmployeesListDto> employees = employeesQueryRepository.getInventory(farmId);
        if(employees.isEmpty())
            throw new GlobalException(HttpStatus.OK, "No se encontraron registros");
        return employees;
    }
}
