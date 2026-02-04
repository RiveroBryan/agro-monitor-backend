package com.agromonitor.api.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReportRequest(
    UUID id, // El UUID generado por Room en Android
    Long productId,
    BigDecimal quantityKg,
    BigDecimal soldPriceUsd,
    String buyerName,
    Instant saleDate,
    String notes
) {}