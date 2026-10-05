package com.project.expensetracker.repository;

import com.project.expensetracker.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    Page<Expense> findByUserId(Long userId, Pageable pageable);

    @Query("""
        SELECT e.category.id, e.category.name, SUM(e.amount)
        FROM Expense e
        WHERE e.user.id = :userId
        AND e.date BETWEEN :startDate AND :endDate
        GROUP BY e.category.id, e.category.name
        """)
    List<Object[]> sumAmountsByCategoryForPeriod(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}