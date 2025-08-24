package com.erp.backend_erp.repositories.cattleDeath;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.deathCattle.CattleDeathEntity;

public interface CattleDeathJPARepository extends JpaRepository<CattleDeathEntity, Integer> {
    
    Optional<CattleDeathEntity> findByIdAndFarmId(Integer id, Long farmId);
    
    // Verificar si un ganado ya está registrado como muerto
    boolean existsByCattleIdAndFarmId(Integer cattleId, Long farmId);
    
    // Verificar si un ternero ya está registrado como muerto
    boolean existsByBirthIdAndFarmId(Integer birthId, Long farmId);
}