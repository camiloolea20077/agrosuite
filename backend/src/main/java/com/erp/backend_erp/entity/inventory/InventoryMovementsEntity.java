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
@Table(name = "inventory_movements")
@Getter
@Setter
@Filter(name = "deletedInventoryMovementsFilter", condition = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE inventory_movements SET deleted_at = NOW() WHERE id=?")
public class InventoryMovementsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventory_id", nullable = false)
    private Long inventoryId;

    @Column(name = "farm_id", nullable = false)
    private Long farmId;

    @Column(name = "tipo_movimiento_id", nullable = false)
    private Long tipoMovimientoId;

    @Column(name = "estado_id", nullable = false)
    private Long estadoId;

    @Column(name = "cantidad", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;

    @Column(name = "cantidad_anterior", precision = 10, scale = 2)
    private BigDecimal cantidadAnterior = BigDecimal.ZERO;

    @Column(name = "cantidad_nueva", precision = 10, scale = 2)
    private BigDecimal cantidadNueva = BigDecimal.ZERO;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento = LocalDateTime.now();

    @Column(name = "fecha_programada")
    private LocalDateTime fechaProgramada;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "numero_documento", length = 100)
    private String numeroDocumento;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "notas")
    private String notas;

    @Column(name = "cantidad_devuelta", precision = 10, scale = 2)
    private BigDecimal cantidadDevuelta = BigDecimal.ZERO;

    @Column(name = "cantidad_usada", precision = 10, scale = 2)
    private BigDecimal cantidadUsada = BigDecimal.ZERO;

    @Column(name = "fecha_devolucion")
    private LocalDateTime fechaDevolucion;

    @Column(name = "esta_cerrado")
    private Boolean estaCerrado = false;

    @Column(name = "ubicacion_origen", length = 100)
    private String ubicacionOrigen;

    @Column(name = "ubicacion_destino", length = 100)
    private String ubicacionDestino;

    @Column(name = "requiere_aprobacion")
    private Boolean requiereAprobacion = false;

    @Column(name = "aprobado_por")
    private Long aprobadoPor;

    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "es_automatico")
    private Boolean esAutomatico = false;

    @Column(name = "movimiento_padre_id")
    private Long movimientoPadreId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

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
        this.deletedAt = LocalDateTime.now();
    }
}
