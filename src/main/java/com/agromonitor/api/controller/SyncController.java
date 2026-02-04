package com.agromonitor.api.controller;


import com.agromonitor.api.dto.response.PriceSyncResponse;
import com.agromonitor.api.dto.response.ReportRequest;
import com.agromonitor.api.model.User;
import com.agromonitor.api.repository.UserRepository;
import com.agromonitor.api.service.impl.PriceService;
import com.agromonitor.api.service.impl.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sync")
@RequiredArgsConstructor
public class SyncController {

    private final PriceService priceService;
    private final ReportService reportService;
    private final UserRepository userRepository;

    // GET /api/v1/sync/prices?lastSync=2026-01-30T10:00:00Z
    @GetMapping("/prices")
    public ResponseEntity<List<PriceSyncResponse>> syncPrices(
            @RequestParam(required = false) Instant lastSync
    ) {
        return ResponseEntity.ok(priceService.getUpdatesSince(lastSync));
    }

    // POST /api/v1/sync/reports
    // Recibe un array de reportes. Retorna 200 OK si procesó el lote.
    @PostMapping("/reports")
    public ResponseEntity<Void> uploadReports(
            @RequestBody List<ReportRequest> reports,
            @AuthenticationPrincipal User currentUser
    ) {
        reportService.syncOfflineReports(reports, currentUser);
        return ResponseEntity.ok().build();
    }
}