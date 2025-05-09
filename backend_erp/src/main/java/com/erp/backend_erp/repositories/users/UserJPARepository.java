package com.erp.backend_erp.repositories.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.backend_erp.entity.UserEntity;

public interface UserJPARepository extends JpaRepository<UserEntity, Long>  {
    public Optional<UserEntity> findByEmail(String email);
}
