package com.project.expensetracker.dto.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseFilter(
        LocalDate startDate,
        LocalDate endDate,
        Long categoryId,
        BigDecimal minAmount,
        BigDecimal maxAmount
) {}