package com.erp.backend_erp.entity.cattleTransfer;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cattle_transfer")
@Getter
@Setter
public class CattleTransferEntity {
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transfer_type", nullable = false)
    private String transferType; // "CATTLE" o "BIRTH"

    @Column(name = "origin_farm_id", nullable = false)
    private Long originFarmId;

    @Column(name = "destination_farm_id", nullable = false)
    private Long destinationFarmId;

    @Column(name = "farm_id", nullable = false)
    private Long farmId;
    
    @Column(name = "transfer_date")
    private String transferDate;

    private String observations;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
