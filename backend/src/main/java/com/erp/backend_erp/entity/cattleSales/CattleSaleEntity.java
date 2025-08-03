package com.erp.backend_erp.entity.cattleSales;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cattle_sales")
@Getter
@Setter
@SQLDelete(sql = "UPDATE cattle_sales SET deleted_at = NOW() WHERE id=?")
public class CattleSaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_venta")
    private String tipoVenta;

    @Column(name = "fecha_venta")
    private String fechaVenta;

    @Column(name = "hora_emision")
    private String horaEmision;

    @Column(name = "numero_factura")
    private String numeroFactura;

    @Column(name = "precio_kilo")
    private Double precioKilo;

    @Column(name = "peso_total")
    private Double pesoTotal;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "estado", nullable = false)
    private String estado = "PENDIENTE"; 

    @Column(name = "iva")
    private BigDecimal iva;

    @Column(name = "descuentos")
    private BigDecimal descuentos;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "moneda")
    private String moneda;

    @Column(name = "forma_pago")
    private String formaPago;

    private String destino;
    private String observaciones;

    @Column(name = "cufe_dian")
    private String cufeDian;

    @Column(name = "farm_id")
    private Long farmId;

    @Column(name = "tercero_id")
    private Long terceroId;

    @ElementCollection
    @CollectionTable(name = "cattle_sales_cattle_ids", joinColumns = @JoinColumn(name = "cattle_sale_id"))
    @Column(name = "cattle_id")
    private List<Long> cattleIds;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CattleSaleItemEntity> items = new ArrayList<>();

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
    public void onDelete() {
        this.deleted_at = LocalDateTime.now();
    }
}