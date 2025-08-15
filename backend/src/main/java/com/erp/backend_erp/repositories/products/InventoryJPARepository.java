package com.erp.backend_erp.repositories.products;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.inventory.InventoryEntity;



public interface InventoryJPARepository extends JpaRepository<InventoryEntity, Long> {
    Optional<InventoryEntity> findByIdAndFarmId(Long id, Long farmId);
    List<InventoryEntity> findByFarmIdOrderByNombreInsumo(Long farmId);
        // Para obtener por ID con validación de finca
    // Optional<InventoryEntity> findByIdAndFarmId(Long id, Long farmId);
}
