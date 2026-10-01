package com.project.expensetracker.dto.expense;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
        @NotNull(message = "Le montant est obligatoire")
        @DecimalMin(value = "0.01", message = "Le montant doit être positif")
        BigDecimal amount,

        @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
        String description,

        @NotNull(message = "La date est obligatoire")
        @PastOrPresent(message = "La date ne peut pas être dans le futur")
        LocalDate date,

        @NotNull(message = "La catégorie est obligatoire")
        Long categoryId
) {}