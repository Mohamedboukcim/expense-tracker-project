package com.project.expensetracker.service;

import com.project.expensetracker.dto.expense.ExpenseFilter;
import com.project.expensetracker.dto.expense.ExpenseRequest;
import com.project.expensetracker.dto.expense.ExpenseResponse;
import com.project.expensetracker.entity.Category;
import com.project.expensetracker.entity.Expense;
import com.project.expensetracker.entity.User;
import com.project.expensetracker.exception.ResourceNotFoundException;
import com.project.expensetracker.mapper.ExpenseMapper;
import com.project.expensetracker.repository.CategoryRepository;
import com.project.expensetracker.repository.ExpenseRepository;
import com.project.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.expensetracker.repository.specification.ExpenseSpecifications.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ExpenseMapper expenseMapper;

    public Page<ExpenseResponse> getFiltered(Long userId, ExpenseFilter filter, Pageable pageable) {
        Specification<Expense> spec = belongsToUser(userId)
                .and(hasCategoryId(filter.categoryId()))
                .and(dateAfterOrEqual(filter.startDate()))
                .and(dateBeforeOrEqual(filter.endDate()))
                .and(amountAfterOrEqual(filter.minAmount()))
                .and(amountBeforeOrEqual(filter.maxAmount()));

        return expenseRepository.findAll(spec, pageable)
                .map(expenseMapper::toResponse);
    }

    public ExpenseResponse create(Long userId, ExpenseRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable"));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Catégorie introuvable");
        }

        Expense expense = Expense.builder()
                .amount(request.amount())
                .description(request.description())
                .date(request.date())
                .category(category)
                .user(user)
                .build();

        Expense saved = expenseRepository.save(expense);
        return expenseMapper.toResponse(saved);
    }

    public ExpenseResponse update(Long userId, Long expenseId, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Dépense introuvable"));

        if (!expense.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Dépense introuvable");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable"));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Catégorie introuvable");
        }

        expense.setAmount(request.amount());
        expense.setDescription(request.description());
        expense.setDate(request.date());
        expense.setCategory(category);

        return expenseMapper.toResponse(expense);
    }

    public void delete(Long userId, Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Dépense introuvable"));

        if (!expense.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Dépense introuvable");
        }

        expenseRepository.delete(expense);
    }
}