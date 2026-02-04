package com.agromonitor.api.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

// Java 21 Record: Conciso y seguro
public record PriceSyncResponse(
    Long id,
    Long productId,
    String productName,
    String category,
    String marketSource,
    BigDecimal priceUsd,
    BigDecimal priceBs,
    LocalDate recordDate,
    Instant updatedAt // Vital para que el móvil actualice su "cursor"
) {}