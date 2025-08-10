package com.erp.backend_erp.repositories.cattleTransfer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.cattleTransfer.CattleTransferEntity;

public interface  CattleTransferJPARepository extends JpaRepository<CattleTransferEntity, Long> {
    
}
