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
@Table(name = "inventory")
@Getter
@Setter
@Filter(name = "deletedIRolesFilter", condition = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE inventory SET deleted_at = NOW() WHERE id=?")
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_interno", unique = true, length = 50)
    private String codigoInterno;

    @Column(name = "nombre_insumo", nullable = false, length = 200)
    private String nombreInsumo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "tipo_insumo_id", nullable = false)
    private Long tipoInsumoId;

    @Column(name = "unidad_medida", nullable = false, length = 20)
    private String unidadMedida;

    @Column(name = "unidad_compra", length = 20)
    private String unidadCompra;

    @Column(name = "factor_conversion", precision = 10, scale = 4)
    private BigDecimal factorConversion = BigDecimal.ONE;

    @Column(name = "cantidad_actual", precision = 10, scale = 2)
    private BigDecimal cantidadActual = BigDecimal.ZERO;

    @Column(name = "cantidad_minima", precision = 10, scale = 2)
    private BigDecimal cantidadMinima = BigDecimal.ZERO;

    @Column(name = "punto_reorden", precision = 10, scale = 2)
    private BigDecimal puntoReorden = BigDecimal.ZERO;

    @Column(name = "cantidad_reservada", precision = 10, scale = 2)
    private BigDecimal cantidadReservada = BigDecimal.ZERO;

    @Column(name = "ubicacion_almacen", length = 100)
    private String ubicacionAlmacen;

    @Column(name = "es_peligroso")
    private Boolean esPeligroso = false;

    @Column(name = "requiere_cuidado_especial")
    private Boolean requiereCuidadoEspecial = false;

    @Column(name = "notas")
    private String notas;

    @Column(name = "estado_id", nullable = false)
    private Long estadoId;

    @Column(name = "farm_id", nullable = false)
    private Long farmId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "deleted_at")
    private LocalDateTime deleted_at;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

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
