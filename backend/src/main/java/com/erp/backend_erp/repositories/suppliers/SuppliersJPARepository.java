package com.erp.backend_erp.repositories.suppliers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.inventory.SuppliersEntity;

public interface SuppliersJPARepository extends JpaRepository<SuppliersEntity, Long> {
    Optional<SuppliersEntity> findById(Long id);
    List<SuppliersEntity> findByActivoOrderByNombre(Long activo);
}
