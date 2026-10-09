package com.project.expensetracker.controller;

import com.project.expensetracker.dto.expense.ExpenseFilter;
import com.project.expensetracker.dto.expense.ExpenseRequest;
import com.project.expensetracker.dto.expense.ExpenseResponse;
import com.project.expensetracker.security.CurrentUserId;
import com.project.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Dépenses", description = "Gestion des dépenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    @Operation(summary = "Lister mes dépenses", description = "Renvoie toutes les dépenses de l'utilisateur connecté")
    public ResponseEntity<Page<ExpenseResponse>> getFiltered(
            @CurrentUserId Long userId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @PageableDefault(size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        ExpenseFilter filter = new ExpenseFilter(startDate, endDate, categoryId, minAmount, maxAmount);
        return ResponseEntity.ok(expenseService.getFiltered(userId, filter, pageable));
    }

    @PostMapping
    @Operation(summary = "Créer une dépense")
    public ResponseEntity<ExpenseResponse> create(
            @CurrentUserId Long userId,
            @Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = expenseService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une dépense")
    public ResponseEntity<ExpenseResponse> update(
            @CurrentUserId Long userId,
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une dépense")
    public ResponseEntity<Void> delete(
            @CurrentUserId Long userId,
            @PathVariable Long id) {
        expenseService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}