package com.project.expensetracker.service;

import com.project.expensetracker.dto.budget.BudgetRequest;
import com.project.expensetracker.dto.budget.BudgetResponse;
import com.project.expensetracker.entity.Budget;
import com.project.expensetracker.entity.Category;
import com.project.expensetracker.entity.User;
import com.project.expensetracker.exception.BudgetAlreadyExistsException;
import com.project.expensetracker.exception.ResourceNotFoundException;
import com.project.expensetracker.mapper.BudgetMapper;
import com.project.expensetracker.repository.BudgetRepository;
import com.project.expensetracker.repository.CategoryRepository;
import com.project.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BudgetMapper budgetMapper;

    public List<BudgetResponse> getAllForUser(Long userId) {
        return budgetRepository.findByUserId(userId)
                .stream()
                .map(budgetMapper::toResponse)
                .toList();
    }

    public BudgetResponse create(Long userId, BudgetRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable"));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Catégorie introuvable");
        }

        if (budgetRepository.existsByCategoryIdAndMonth(request.categoryId(), request.month())) {
            throw new BudgetAlreadyExistsException(
                    "Un budget existe déjà pour cette catégorie sur " + request.month());
        }

        Budget budget = Budget.builder()
                .monthlyLimit(request.monthlyLimit())
                .month(request.month())
                .category(category)
                .user(user)
                .build();

        Budget saved = budgetRepository.save(budget);
        return budgetMapper.toResponse(saved);
    }

    public BudgetResponse update(Long userId, Long budgetId, BudgetRequest request) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget introuvable"));

        if (!budget.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Budget introuvable");
        }

        boolean monthOrCategoryChanged = !budget.getMonth().equals(request.month())
                || !budget.getCategory().getId().equals(request.categoryId());

        if (monthOrCategoryChanged
                && budgetRepository.existsByCategoryIdAndMonth(request.categoryId(), request.month())) {
            throw new BudgetAlreadyExistsException(
                    "Un budget existe déjà pour cette catégorie sur " + request.month());
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable"));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Catégorie introuvable");
        }

        budget.setMonthlyLimit(request.monthlyLimit());
        budget.setMonth(request.month());
        budget.setCategory(category);

        return budgetMapper.toResponse(budget);
    }

    public void delete(Long userId, Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget introuvable"));

        if (!budget.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Budget introuvable");
        }

        budgetRepository.delete(budget);
    }
}