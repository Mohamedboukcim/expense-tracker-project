package com.project.expensetracker.controller;

import com.project.expensetracker.dto.category.CategoryRequest;
import com.project.expensetracker.dto.category.CategoryResponse;
import com.project.expensetracker.security.CurrentUserId;
import com.project.expensetracker.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Catégories", description = "Gestion des catégories de dépenses")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Lister mes catégories", description = "Renvoie toutes les catégories de l'utilisateur connecté")
    public ResponseEntity<List<CategoryResponse>> getAll(@CurrentUserId Long userId) {
        return ResponseEntity.ok(categoryService.getAllForUser(userId));
    }

    @PostMapping
    @Operation(summary = "Créer une catégorie")
    public ResponseEntity<CategoryResponse> create(
            @CurrentUserId Long userId,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une catégorie")
    public ResponseEntity<CategoryResponse> update(
            @CurrentUserId Long userId,
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.update(userId, id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une catégorie")
    public ResponseEntity<Void> delete(
            @CurrentUserId Long userId,
            @PathVariable Long id) {
        categoryService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}