package com.agromonitor.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "daily_prices", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "market_source", "record_date"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyPrice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "market_source", nullable = false)
    private String marketSource; // Ej: MERCABAR

    @Column(name = "price_usd", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceUsd;

    @Column(name = "price_bs", nullable = false, precision = 12, scale = 2)
    private BigDecimal priceBs;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;
}