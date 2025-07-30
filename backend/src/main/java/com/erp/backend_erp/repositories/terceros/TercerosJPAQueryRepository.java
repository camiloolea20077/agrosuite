package com.erp.backend_erp.repositories.terceros;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.terceros.TercerosEntity;

public interface TercerosJPAQueryRepository extends JpaRepository<TercerosEntity, Long>{
    public Optional<TercerosEntity> existsByNumeroIdentificacion(String numeroIdentificacion);
    public Optional<TercerosEntity> findByIdAndFarmId(Long id, Long farmId);
    
}
