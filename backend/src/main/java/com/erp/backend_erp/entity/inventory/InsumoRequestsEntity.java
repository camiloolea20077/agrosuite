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
@Table(name = "insumo_requests")
@Getter
@Setter
@Filter(name = "deletedInsumoRequestsFilter", condition = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE insumo_requests SET deleted_at = NOW() WHERE id=?")
public class InsumoRequestsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_solicitud", nullable = false, unique = true, length = 50)
    private String numeroSolicitud;

    @Column(name = "farm_id", nullable = false)
    private Long farmId;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    @Column(name = "fecha_necesaria")
    private LocalDateTime fechaNecesaria;

    @Column(name = "prioridad")
    private Long prioridad;

    @Column(name = "justificacion")
    private String justificacion;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "estado")
    private Long estado;

    @Column(name = "solicitado_por", nullable = false)
    private Long solicitadoPor;

    @Column(name = "procesado_por")
    private Long procesadoPor;

    @Column(name = "fecha_procesamiento")
    private LocalDateTime fechaProcesamiento;

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
