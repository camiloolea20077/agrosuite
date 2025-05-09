package com.erp.backend_erp.entity.companies;

import java.time.LocalDateTime;

import org.hibernate.annotations.Filter;
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
@Table(name = "companies")
@Getter
@Setter
@Filter(name = "deletedCompaniesFilter", condition = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE companies SET deleted_at = NOW() WHERE id=?")
public class CompaniesEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=false)
    private String name;

    @Column(nullable=false, unique=false)
    private String plan;

    @Column(nullable=true, unique=false)
    private String nit;

    @Column(nullable=false, unique=false)
    private String email;

    @Column(nullable=false, unique=false)
    private String phone;

    @Column(nullable=false, unique=false)
    private String address;

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
