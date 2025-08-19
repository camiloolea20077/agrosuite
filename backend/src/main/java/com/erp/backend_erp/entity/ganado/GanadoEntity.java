package com.erp.backend_erp.entity.ganado;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cattle")
@Getter
@Setter
@SQLDelete(sql = "UPDATE cattle SET deleted_at = NOW() WHERE id=?")
public class GanadoEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String tipo_ganado;

    private String numero_ganado;

    private String sexo;

    private String color;

    private String peso;

    private String fecha_nacimiento;

    private String lote_ganado;

    private String observaciones;

    private Long activo;

    @Column(name = "farm_id")
    private Long farmId;

    @Column(name = "tipo_animal")
    private String tipo_animal;

    @Column(name = "embarazada")
    private Integer embarazada;

    @Column(name = "fecha_embarazo")
    private String fecha_embarazo;

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
