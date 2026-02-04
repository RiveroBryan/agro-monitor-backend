package com.agromonitor.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReport {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity_kg", nullable = false)
    private BigDecimal quantityKg;

    @Column(name = "sold_price_usd", nullable = false)
    private BigDecimal soldPriceUsd;

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "sale_date", nullable = false)
    private Instant saleDate;

    @Column(name = "uploaded_at")
    private Instant uploadedAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {
        this.uploadedAt = Instant.now();
    }
}