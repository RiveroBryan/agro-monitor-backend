package com.agromonitor.api.service.impl;


import com.agromonitor.api.dto.response.ReportRequest;
import com.agromonitor.api.model.Product;
import com.agromonitor.api.model.User;
import com.agromonitor.api.model.UserReport;
import com.agromonitor.api.repository.ProductRepository;
import com.agromonitor.api.repository.UserReportRepository;
import com.agromonitor.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserReportRepository reportRepository;
    private final ProductRepository productRepository;
    private final PriceService priceService;

    @Transactional
    public void syncOfflineReports(List<ReportRequest> requests, User currentUser) {
        // --- FASE 1: Guardar todos los reportes ---
        for (ReportRequest req : requests) {
            // 1. Idempotencia
            if (reportRepository.existsById(req.id())) {
                continue;
            }

            // 2. Validar referencias
            Product product = productRepository.findById(req.productId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + req.productId()));

            // 3. Crear entidad
            UserReport report = UserReport.builder()
                    .id(req.id())
                    .userId(currentUser.getId())
                    .product(product)
                    .quantityKg(req.quantityKg())
                    .soldPriceUsd(req.soldPriceUsd())
                    .buyerName(req.buyerName())
                    .saleDate(req.saleDate())
                    .notes(req.notes())
                    .build();

            reportRepository.save(report);
        }

        // --- FASE 2: Recalcular Precios (FUERA DEL BUCLE) ---
        // Usamos 'requests' (el nombre correcto) para obtener los IDs únicos afectados
        Set<Long> affectedProductIds = requests.stream()
                .map(ReportRequest::productId) // Usamos el accessor del Record
                .collect(Collectors.toSet());

        // Actualizamos el precio comunitario solo una vez por producto afectado
        LocalDate today = LocalDate.now();
        for (Long productId : affectedProductIds) {
            priceService.updateCommunityPrice(productId, today);
        }
    }

}