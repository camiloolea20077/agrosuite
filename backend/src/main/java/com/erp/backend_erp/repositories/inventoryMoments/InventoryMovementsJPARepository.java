package com.erp.backend_erp.repositories.inventoryMoments;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.inventory.InventoryMovementsEntity;

public interface InventoryMovementsJPARepository extends JpaRepository<InventoryMovementsEntity, Long> {

        Optional<InventoryMovementsEntity> findById(Long id);

        Optional<InventoryMovementsEntity> findByIdAndFarmId(Long id, Long farmId);

        List<InventoryMovementsEntity> findByFarmIdOrderByFechaMovimientoDesc(Long farmId);

        List<InventoryMovementsEntity> findByInventoryIdAndFarmIdOrderByFechaMovimientoDesc(Long inventoryId,
                        Long farmId);

        // En tu JPA Repository Interface
        List<InventoryMovementsEntity> findByFarmIdAndFechaMovimientoBetweenAndDeletedAtIsNullOrderByFechaMovimientoDesc(
        Long farmId, 
        LocalDateTime startDate, 
        LocalDateTime endDate
        );
        List<InventoryMovementsEntity> findByFarmIdAndRequiereAprobacionAndAprobadoPorIsNull(
                        Long farmId,
                        Boolean requiereAprobacion);
}