package com.agromonitor.api.repository;

import com.agromonitor.api.model.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;
@Repository

public interface UserReportRepository extends JpaRepository<UserReport, UUID> {

    @Query("SELECT SUM(u.soldPriceUsd) / SUM(u.quantityKg) FROM UserReport u " +
            "WHERE u.product.id = :productId AND u.saleDate >= :startOfDay AND u.saleDate < :endOfDay")
    Double getAveragePriceForProductOnDate(Long productId, Instant startOfDay, Instant endOfDay);
}