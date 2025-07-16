package com.erp.backend_erp.repositories.sales;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.sales.SaleEntity;

public interface SalesJPARepository  extends JpaRepository<SaleEntity, Long> {
    
}
