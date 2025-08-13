package com.erp.backend_erp.entity.cattleTransfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cattle_transfer_item")
@Getter
@Setter
public class CattleTransferItemEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transfer_id", nullable = false)
    private Long transferId; 

    @Column(name = "cattle_id")
    private Long cattleId;

    @Column(name = "birth_id")
    private Long birthId;

    // Nuevos campos agregados
    @Column(name = "numero_ganado")
    private String numero_ganado;

    @Column(name = "peso", precision = 10, scale = 2)
    private BigDecimal peso;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
    }
}
