package com.agromonitor.api.service.impl;

import com.agromonitor.api.dto.response.PriceSyncResponse;
import com.agromonitor.api.model.DailyPrice;
import com.agromonitor.api.repository.DailyPriceRepository;
import com.agromonitor.api.repository.ProductRepository;
import com.agromonitor.api.repository.UserReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final DailyPriceRepository priceRepository;
    private final UserReportRepository reportRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<PriceSyncResponse> getUpdatesSince(Instant lastSync) {
        // 1. Si lastSync es null (primera vez), traemos t0do
        // 2. Si tiene fecha, traemos solo los modificados
        List<DailyPrice> entities = (lastSync == null)
            ? priceRepository.findAllByOrderByRecordDateDesc()
            : priceRepository.findChangesSince(lastSync);

        // 3. Mapeamos a DTO (Java 21 Stream API)
        return entities.stream()
            .map(this::mapToDto)
            .toList();
    }

    private PriceSyncResponse mapToDto(DailyPrice p) {
        return new PriceSyncResponse(
                p.getId(),
                p.getProduct().getId(),
                p.getProduct().getName(),
                p.getProduct().getCategory(),
                p.getMarketSource(),
                p.getPriceUsd(),
                p.getPriceBs(),
                p.getRecordDate(),
                p.getUpdatedAt()
        );
    }


    @Transactional
    public void updateCommunityPrice(Long productId, LocalDate date) {
        // CORRECCIÓN: Quitamos .toEpochMilli() para quedarnos con el objeto Instant
        Instant startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // Ahora pasamos objetos Instant compatibles con JPA
        Double averagePrice = reportRepository.getAveragePriceForProductOnDate(productId, startOfDay, endOfDay);

        if (averagePrice == null) return; // No hubo ventas hoy
        // 2. Buscar si ya existe un precio de la comunidad para hoy
        DailyPrice communityPrice = priceRepository.findByProductIdAndRecordDateAndMarketSource(
                productId, date, "COMUNIDAD_AGRO"
        ).orElse(DailyPrice.builder()
                .product(productRepository.getReferenceById(productId))
                .recordDate(date)
                .marketSource("COMUNIDAD_AGRO") // Fuente especial
                .build());

        // 3. Actualizar valores
        communityPrice.setPriceUsd(BigDecimal.valueOf(averagePrice));
        // Asumimos tasa fija o la calculas. Por simpleza, multiplicamos x 50
        communityPrice.setPriceBs(BigDecimal.valueOf(averagePrice * 50));

        priceRepository.save(communityPrice);
    }
}