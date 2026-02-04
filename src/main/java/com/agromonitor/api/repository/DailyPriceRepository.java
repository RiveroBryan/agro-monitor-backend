package com.agromonitor.api.repository;

import com.agromonitor.api.model.DailyPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyPriceRepository extends JpaRepository<DailyPrice, Long> {

    // QUERY CRÍTICO PARA OFFLINE-FIRST:
    // "Dame todos los precios que hayan sido creados O modificados después de X fecha"
    @Query("SELECT p FROM DailyPrice p WHERE p.updatedAt > :lastSync")
    List<DailyPrice> findChangesSince(@Param("lastSync") Instant lastSync);
    
    // Para cuando el usuario hace pull completo por primera vez
    List<DailyPrice> findAllByOrderByRecordDateDesc();

    Optional<DailyPrice> findByProductIdAndRecordDateAndMarketSource(Long productId, LocalDate date, String comunidadAgro);
}