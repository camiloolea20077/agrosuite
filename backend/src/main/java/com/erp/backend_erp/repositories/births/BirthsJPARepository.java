package com.erp.backend_erp.repositories.births;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.births.BirthsEntity;

public interface BirthsJPARepository extends JpaRepository<BirthsEntity, Long> {
    public Optional<BirthsEntity> findByNumeroCria(String numeroCria);
    
}
