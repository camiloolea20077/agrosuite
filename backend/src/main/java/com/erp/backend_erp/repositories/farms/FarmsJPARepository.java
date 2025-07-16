package com.erp.backend_erp.repositories.farms;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.farm.FarmEntity;

public interface FarmsJPARepository  extends JpaRepository <FarmEntity, Long> {
    public Optional<FarmEntity> existsByNombre(String nombre);
}
