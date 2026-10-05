package com.project.expensetracker.repository;

import com.project.expensetracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByUserId(Long userId);
    Optional<Budget> findByCategoryIdAndMonth(Long categoryId, YearMonth month);
    boolean existsByCategoryIdAndMonth(Long categoryId, YearMonth month);
}