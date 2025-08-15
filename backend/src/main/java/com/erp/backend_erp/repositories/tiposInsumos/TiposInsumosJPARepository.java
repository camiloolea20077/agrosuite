package com.erp.backend_erp.repositories.tiposInsumos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.inventory.TiposInsumosEntity;

public interface TiposInsumosJPARepository extends JpaRepository<TiposInsumosEntity, Long> {
    Optional<TiposInsumosEntity> findById(Long id);
    List<TiposInsumosEntity> findByActivoOrderByNombre(Long activo);
}
