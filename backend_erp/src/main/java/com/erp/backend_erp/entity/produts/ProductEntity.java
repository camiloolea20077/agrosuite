package com.erp.backend_erp.entity.produts;

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
@Table(name = "products")
@Getter
@Setter
@Filter(name = "deletedIRolesFilter", condition = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE products SET deleted_at = NOW() WHERE id=?")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long company_id;

    @Column(nullable = false)
    private String name;

    private String sku;

    private BigDecimal price;

    private BigDecimal quantity;

    private String code;

    private String description;

    private LocalDateTime entry_date;

    private Long supplier_id;

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
