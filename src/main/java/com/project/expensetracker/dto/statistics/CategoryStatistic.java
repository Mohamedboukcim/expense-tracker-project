package com.project.expensetracker.dto.statistics;

import java.math.BigDecimal;

public record CategoryStatistic(
        Long categoryId,
        String categoryName,
        BigDecimal totalSpent,
        BigDecimal budgetLimit,
        BigDecimal remaining
) {}