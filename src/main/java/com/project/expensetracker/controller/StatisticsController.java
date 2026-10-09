package com.project.expensetracker.controller;

import com.project.expensetracker.dto.statistics.MonthlyStatistics;
import com.project.expensetracker.security.CurrentUserId;
import com.project.expensetracker.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistiques", description = "Gestion des tatistiques")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping
    @Operation(summary = "Lister mes statistiques", description = "Renvoie toutes les statistiques de l'utilisateur connecté")
    public ResponseEntity<MonthlyStatistics> getMonthlyStatistics(
            @CurrentUserId Long userId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(statisticsService.getMonthlyStatistics(userId, month));
    }
}