package com.project.expensetracker.dto.budget;

import java.math.BigDecimal;
import java.time.YearMonth;

public record BudgetResponse(
        Long id,
        BigDecimal monthlyLimit,
        YearMonth month,
        Long categoryId,
        String categoryName
) {}