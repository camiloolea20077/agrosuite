package com.erp.backend_erp.entity.inventory;

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
@Table(name = "inventory_suppliers")
@Getter
@Setter
public class InventorySuppliersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventory_id", nullable = false)
    private Long inventoryId;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "codigo_proveedor", length = 100)
    private String codigoProveedor;

    @Column(name = "precio_referencia", precision = 12, scale = 2)
    private BigDecimal precioReferencia = BigDecimal.ZERO;

    @Column(name = "es_principal")
    private Long esPrincipal;

    @Column(name = "activo")
    private Long activo ;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
    }
}
