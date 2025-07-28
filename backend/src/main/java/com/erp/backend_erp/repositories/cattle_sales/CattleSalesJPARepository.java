package com.erp.backend_erp.repositories.cattle_sales;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.cattleSales.CattleSaleEntity;



public interface CattleSalesJPARepository extends JpaRepository<CattleSaleEntity, Long> {
    Optional<CattleSaleEntity> findByIdAndFarmId(Long id, Long farmId);
}