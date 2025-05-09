package com.erp.backend_erp.repositories.clients;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.clients.ClientsEntity;
import com.erp.backend_erp.entity.companies.CompaniesEntity;

public interface ClientsJPARepository extends JpaRepository<ClientsEntity, Long> {
    public Optional<CompaniesEntity> findByDocument(String name);
}
