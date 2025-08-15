package com.erp.backend_erp.repositories.estadosInventarios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.inventory.EstadosInventarioEntity;

public interface EstadosInventarioJPARepository extends JpaRepository<EstadosInventarioEntity, Long> {
    Optional<EstadosInventarioEntity> findById(Long id);
    List<EstadosInventarioEntity> findByActivoOrderByNombre(Long activo);
}