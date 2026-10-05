package com.project.expensetracker.dto.budget;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.YearMonth;

public record BudgetRequest(
        @NotNull(message = "Le plafond mensuel est obligatoire")
        @DecimalMin(value = "0.01", message = "Le plafond doit être positif")
        BigDecimal monthlyLimit,

        @NotNull(message = "Le mois est obligatoire")
        YearMonth month,

        @NotNull(message = "La catégorie est obligatoire")
        Long categoryId
) {}