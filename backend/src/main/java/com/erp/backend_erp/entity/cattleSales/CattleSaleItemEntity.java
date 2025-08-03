package com.erp.backend_erp.entity.cattleSales;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cattle_sale_items")
@Getter
@Setter
@SQLDelete(sql = "UPDATE cattle_sale_items SET deleted_at = NOW() WHERE id = ?")
public class CattleSaleItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private CattleSaleEntity sale;

    @Column(name = "tipo_origen", nullable = false)
    private String tipoOrigen;

    @Column(name = "id_origen", nullable = false)
    private Long idOrigen;

    @Column(name = "farm_id", nullable = false)
    private Long farmId;
        
    @Column(name = "numero_ganado", nullable = false)
    private String numero_ganado;

    @Column(name = "peso_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal pesoVenta;

    @Column(name = "precio_kilo", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioKilo;

    @Column(name = "precio_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioTotal;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "deleted_at")
    private LocalDateTime deleted_at;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }

    @PreRemove
    protected void onDelete() {
        deleted_at = LocalDateTime.now();
    }
}
