package com.erp.backend_erp.repositories.products;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.produts.ProductEntity;
import com.erp.backend_erp.entity.role.RoleEntity;


public interface ProductsJPARepository extends JpaRepository<ProductEntity, Long> {
    public Optional<RoleEntity> existsByCode(String code);
}
