package com.erp.backend_erp.services.implementations;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.roles.CreateRoleDto;
import com.erp.backend_erp.dto.roles.RoleDto;
import com.erp.backend_erp.dto.roles.UpdateRoleDto;
import com.erp.backend_erp.entity.role.RoleEntity;
import com.erp.backend_erp.mappers.roles.RolesMapper;
import com.erp.backend_erp.repositories.role.RoleJPARepository;
import com.erp.backend_erp.repositories.role.RoleQueryRepository;
import com.erp.backend_erp.services.RolesService;
import com.erp.backend_erp.util.GlobalException;

@Service
public class RolesServiceImpl implements RolesService {

    private final RoleQueryRepository roleRepository;
    private final RolesMapper roleMapper;
    private final RoleJPARepository roleJPARepository;

    public RolesServiceImpl(RoleQueryRepository roleRepository, RolesMapper roleMapper, RoleJPARepository roleJPARepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.roleJPARepository = roleJPARepository;
    }

    @Override
    public RoleDto create(CreateRoleDto createRoleDto) {
        Boolean exists = roleRepository.existsByName(createRoleDto.getName().toLowerCase());
        if (exists)
            throw new GlobalException(HttpStatus.BAD_REQUEST, "El NIT de la empresa ya se encuentra registrado");

        try {
            RoleEntity rolesEntity = roleMapper
                    .createToEntity(createRoleDto);
            RoleEntity saveRolesEntity = roleJPARepository
                    .save(rolesEntity);
                    return roleMapper.toDto(saveRolesEntity);
        } catch (Exception e) {
            System.err.println("Error al crear el usuario: " + e.toString());
            e.printStackTrace();
            throw new RuntimeException("Error al crear el usuario: " + e.getMessage(), e);
        }
    }

    @Override
	public Boolean delete(Long id) {
		roleJPARepository.findById(id)
				.orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
        roleJPARepository.deleteById(id);
		return true;
	}

    @Override
	public Boolean update(UpdateRoleDto updateRoleDto) {

		RoleEntity roleEntity = roleJPARepository.findById(updateRoleDto.getId())
				.orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, "Elemento no encontrado"));
		try {
			roleMapper.updateEntityFromDto(updateRoleDto, roleEntity);
			roleJPARepository.save(roleEntity);
			return true;
		}
		catch (Exception e) {
			throw new RuntimeException("Error al actualizar el registro");
		}
	}
}
