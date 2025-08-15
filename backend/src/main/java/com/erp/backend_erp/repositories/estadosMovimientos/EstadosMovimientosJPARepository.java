package com.erp.backend_erp.repositories.estadosMovimientos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.inventory.EstadosMovimientosEntity;

public interface EstadosMovimientosJPARepository extends JpaRepository<EstadosMovimientosEntity, Long> {
    Optional<EstadosMovimientosEntity> findById(Long id);
    List<EstadosMovimientosEntity> findByActivoOrderByNombre(Long activo);
}