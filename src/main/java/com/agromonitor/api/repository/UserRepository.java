package com.agromonitor.api.repository;

import com.agromonitor.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Método crítico usado por UserDetailsServiceImpl para el Login
    Optional<User> findByEmail(String email);

    // Validación rápida para el registro de nuevos usuarios
    boolean existsByEmail(String email);
}