package com.erp.backend_erp.repositories.cattle_sales;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.cattleSales.OtpRequestEntity;

public interface OtpRequestJPARepository extends JpaRepository<OtpRequestEntity, Long> {
    Optional<OtpRequestEntity> findByToken(String token);
}
