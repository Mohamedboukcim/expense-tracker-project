package com.project.expensetracker.mapper;

import com.project.expensetracker.dto.budget.BudgetResponse;
import com.project.expensetracker.entity.Budget;
import org.springframework.stereotype.Component;

@Component
public class BudgetMapper {

    public BudgetResponse toResponse(Budget budget) {
        return new BudgetResponse(
                budget.getId(),
                budget.getMonthlyLimit(),
                budget.getMonth(),
                budget.getCategory().getId(),
                budget.getCategory().getName()
        );
    }
}