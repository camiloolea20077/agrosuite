package com.erp.backend_erp.services;

import com.erp.backend_erp.dto.roles.CreateRoleDto;
import com.erp.backend_erp.dto.roles.RoleDto;
import com.erp.backend_erp.dto.roles.UpdateRoleDto;


public interface RolesService {
    RoleDto create(CreateRoleDto dto);
    Boolean delete(Long id);
    Boolean update(UpdateRoleDto dto);
}
