package com.erp.backend_erp.entity.deathCattle;

import java.time.LocalDateTime;

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
@Table(name = "cattle_deaths")
@Getter
@Setter
public class CattleDeathEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cattle_id")
    private Integer cattleId;

    @Column(name = "birth_id")
    private Integer birthId;

    @Column(name = "fecha_muerte", nullable = false, length = 10)
    private String fechaMuerte;

    @Column(name = "motivo_muerte", nullable = false, length = 100)
    private String motivoMuerte;

    @Column(name = "descripcion_detallada", columnDefinition = "TEXT")
    private String descripcionDetallada;

    @Column(name = "peso_muerte", length = 50)
    private String pesoMuerte;

    @Column(name = "causa_categoria", length = 50)
    private String causaCategoria;

    @Column(name = "farm_id", nullable = false)
    private Long farmId;

    @Column(name = "usuario_registro")
    private Long usuarioRegistro;

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
