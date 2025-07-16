package com.erp.backend_erp.repositories.ganado;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.ganado.GanadoEntity;

public interface GanadoJPARepository extends JpaRepository<GanadoEntity, Long> {
    public Optional<GanadoEntity> findById(Long id);
}
