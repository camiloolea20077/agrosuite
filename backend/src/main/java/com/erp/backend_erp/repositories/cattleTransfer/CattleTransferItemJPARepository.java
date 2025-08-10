package com.erp.backend_erp.repositories.cattleTransfer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.cattleTransfer.CattleTransferItemEntity;

public  interface CattleTransferItemJPARepository extends JpaRepository<CattleTransferItemEntity, Long> {
    List<CattleTransferItemEntity> findAllByTransferId(Long transferId);
}
