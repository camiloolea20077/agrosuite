package com.erp.backend_erp.repositories.tiposMovimientos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.inventory.TiposMovimientosEntity;

public interface TiposMovimientosJPARepository extends JpaRepository<TiposMovimientosEntity, Long> {

    Optional<TiposMovimientosEntity> findById(Long id);

    // Catálogo activo
    List<TiposMovimientosEntity> findByActivoOrderByNombre(Long activo);

    // Filtros por tipo (flexibles)
    List<TiposMovimientosEntity> findByEsEntradaAndEsSalidaAndActivoOrderByNombre(Long esEntrada, Long esSalida, Long activo);

    List<TiposMovimientosEntity> findByEsEntradaAndActivoOrderByNombre(Long esEntrada, Long activo);

    List<TiposMovimientosEntity> findByEsSalidaAndActivoOrderByNombre(Long esSalida, Long activo);
}