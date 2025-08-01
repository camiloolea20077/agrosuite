package com.erp.backend_erp.repositories.role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.role.Role;
import com.erp.backend_erp.entity.role.RoleEntity;

public interface RoleJPARepository extends JpaRepository<RoleEntity, Long> {
    Optional<Role> findByNombre(String nombre);
    public Optional<RoleEntity> existsByNombre(String nombre);
}
