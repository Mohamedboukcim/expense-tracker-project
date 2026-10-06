package com.project.expensetracker.messaging;

import java.math.BigDecimal;
import java.time.YearMonth;

public record BudgetAlertMessage(
        Long userId,
        String userEmail,
        Long categoryId,
        String categoryName,
        YearMonth month,
        BigDecimal budgetLimit,
        BigDecimal totalSpent
) {}