package com.erp.backend_erp.services.implementations;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.companies.CompaniesDto;
import com.erp.backend_erp.dto.companies.CreateCompaniesDto;
import com.erp.backend_erp.dto.companies.UpdateCompaniesDto;
import com.erp.backend_erp.entity.companies.CompaniesEntity;
import com.erp.backend_erp.mappers.company.CompanyMapper;
import com.erp.backend_erp.repositories.companies.CompaniesJPARepository;
import com.erp.backend_erp.repositories.companies.CompaniesQueryRepository;
import com.erp.backend_erp.services.CompanieService;
import com.erp.backend_erp.util.GlobalException;

@Service
public class CompaniesServicesImpl implements CompanieService {


    private final CompaniesQueryRepository companiesQueryRepository;
    private final CompaniesJPARepository companiesJPARepository;
    private final CompanyMapper companyMapper;

    public CompaniesServicesImpl(CompaniesQueryRepository companiesQueryRepository, CompaniesJPARepository companiesJPARepository, CompanyMapper companyMapper) {
        this.companiesQueryRepository = companiesQueryRepository;
        this.companyMapper = companyMapper;
        this.companiesJPARepository = companiesJPARepository;
    }


    @Override
    public CompaniesDto create(CreateCompaniesDto createCompaniesDto) {
        Boolean exists = companiesQueryRepository.existsByNit(createCompaniesDto.getNit().toLowerCase());
        if (exists)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El NIT de la empresa ya se encuentra registrado");

        try {
            CompaniesEntity companiesEntity = companyMapper
                    .createToEntity(createCompaniesDto);
            CompaniesEntity saveCompaniesEntity = companiesJPARepository
                    .save(companiesEntity);
                    return companyMapper.toDto(saveCompaniesEntity);
        } catch (Exception e) {
            System.err.println("Error al crear el usuario: " + e.toString());
            e.printStackTrace();
            throw new RuntimeException("Error al crear el usuario: " + e.getMessage(), e);
        }
    }

    @Override
	public Boolean update(UpdateCompaniesDto updateCompaniesDto) {

		CompaniesEntity roleEntity = companiesJPARepository.findById(updateCompaniesDto.getId())
				.orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
		try {
			companyMapper.updateEntityFromDto(updateCompaniesDto, roleEntity);
			companiesJPARepository.save(roleEntity);
			return true;
		}
		catch (Exception e) {
            e.printStackTrace();
			throw new RuntimeException("Error al actualizar el registro");
		}
	}
    
    @Override
	public Boolean delete(Long id) {
		companiesJPARepository.findById(id)
				.orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        companiesJPARepository.deleteById(id);
		return true;
	}

}
