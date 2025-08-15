package com.erp.backend_erp.entity.inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@Filter(name = "deletedPurchaseOrdersFilter", condition = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE purchase_orders SET deleted_at = NOW() WHERE id=?")
public class PurchaseOrdersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_orden", nullable = false, unique = true, length = 50)
    private String numeroOrden;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "farm_id", nullable = false)
    private Long farmId;

    @Column(name = "insumo_request_id")
    private Long insumoRequestId;

    @Column(name = "fecha_orden")
    private LocalDateTime fechaOrden = LocalDateTime.now();

    @Column(name = "fecha_entrega_esperada")
    private LocalDateTime fechaEntregaEsperada;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    @Column(name = "subtotal", precision = 12, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "impuestos", precision = 12, scale = 2)
    private BigDecimal impuestos = BigDecimal.ZERO;

    @Column(name = "total", precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "estado")
    private Long estado;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "deleted_at")
    private LocalDateTime deleted_at;

    @Column(name = "created_by")
    private Long createdBy;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }

    @PreRemove
    public void onDelete() {
        this.deleted_at = LocalDateTime.now();
    }
}
