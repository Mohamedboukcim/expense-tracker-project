package com.project.expensetracker.dto.statistics;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public record MonthlyStatistics(
        YearMonth month,
        BigDecimal totalSpent,
        List<CategoryStatistic> byCategory
) {}