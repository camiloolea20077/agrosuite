package com.erp.backend_erp.repositories.companies;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.companies.CompaniesEntity;

public interface  CompaniesJPARepository extends JpaRepository<CompaniesEntity, Long> {
    public Optional<CompaniesEntity> findByName(String name);
}
