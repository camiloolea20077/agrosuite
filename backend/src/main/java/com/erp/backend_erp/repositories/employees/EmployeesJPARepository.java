package com.erp.backend_erp.repositories.employees;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.employees.EmployeesEntity;

public interface EmployeesJPARepository extends JpaRepository<EmployeesEntity, Long> {
    public Optional<EmployeesEntity> findByIdentificacion(String identificacion);
}
