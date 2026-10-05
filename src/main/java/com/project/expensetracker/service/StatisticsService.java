package com.project.expensetracker.service;

import com.project.expensetracker.dto.statistics.CategoryStatistic;
import com.project.expensetracker.dto.statistics.MonthlyStatistics;
import com.project.expensetracker.entity.Budget;
import com.project.expensetracker.repository.BudgetRepository;
import com.project.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;

    public MonthlyStatistics getMonthlyStatistics(Long userId, YearMonth month) {
        List<Object[]> rawResults = expenseRepository.sumAmountsByCategoryForPeriod(
                userId, month.atDay(1), month.atEndOfMonth());

        List<Budget> budgets = budgetRepository.findByUserId(userId).stream()
                .filter(b -> b.getMonth().equals(month))
                .toList();

        Map<Long, BigDecimal> budgetByCategoryId = budgets.stream()
                .collect(java.util.stream.Collectors.toMap(
                        b -> b.getCategory().getId(),
                        Budget::getMonthlyLimit));

        List<CategoryStatistic> byCategory = rawResults.stream()
                .map(row -> {
                    Long categoryId = (Long) row[0];
                    String categoryName = (String) row[1];
                    BigDecimal totalSpent = (BigDecimal) row[2];
                    BigDecimal budgetLimit = budgetByCategoryId.get(categoryId);
                    BigDecimal remaining = budgetLimit != null ? budgetLimit.subtract(totalSpent) : null;

                    return new CategoryStatistic(categoryId, categoryName, totalSpent, budgetLimit, remaining);
                })
                .toList();

        BigDecimal totalSpent = byCategory.stream()
                .map(CategoryStatistic::totalSpent)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MonthlyStatistics(month, totalSpent, byCategory);
    }
}