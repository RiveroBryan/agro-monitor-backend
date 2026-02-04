package com.agromonitor.api.repository;

import com.agromonitor.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Para listar solo productos que se deben mostrar en la App
    List<Product> findByIsActiveTrue();
    
    // Útil si necesitas buscar por nombre exacto (ej. validaciones de carga masiva)
    Optional<Product> findByName(String name);
}