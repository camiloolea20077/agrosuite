package com.erp.backend_erp.entity.inventory;

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
@Table(name = "tipos_movimientos")
@Getter
@Setter
@Filter(name = "deletedTiposMovimientosFilter", condition = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE tipos_movimientos SET deleted_at = NOW() WHERE id=?")
public class TiposMovimientosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "es_entrada", nullable = false)
    private Long esEntrada;

    @Column(name = "es_salida", nullable = false)
    private Long esSalida;

    @Column(name = "requiere_empleado")
    private Long requiereEmpleado;

    @Column(name = "requiere_aprobacion")
    private Long requiereAprobacion;

    @Column(name = "activo")
    private Long activo;

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
