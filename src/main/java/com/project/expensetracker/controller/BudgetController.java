package com.project.expensetracker.controller;

import com.project.expensetracker.dto.budget.BudgetRequest;
import com.project.expensetracker.dto.budget.BudgetResponse;
import com.project.expensetracker.security.CurrentUserId;
import com.project.expensetracker.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@Tag(name = "Budgets", description = "Gestion des budgets")
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    @Operation(summary = "Lister mes budgets", description = "Renvoie toutes les budgets de l'utilisateur connecté")
    public ResponseEntity<List<BudgetResponse>> getAll(@CurrentUserId Long userId) {
        return ResponseEntity.ok(budgetService.getAllForUser(userId));
    }

    @PostMapping
    @Operation(summary = "Créer un budget")
    public ResponseEntity<BudgetResponse> create(
            @CurrentUserId Long userId,
            @Valid @RequestBody BudgetRequest request) {
        BudgetResponse response = budgetService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un budget")
    public ResponseEntity<BudgetResponse> update(
            @CurrentUserId Long userId,
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un budget")
    public ResponseEntity<Void> delete(
            @CurrentUserId Long userId,
            @PathVariable Long id) {
        budgetService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}