package com.project.expensetracker.controller;

import com.project.expensetracker.dto.expense.ExpenseFilter;
import com.project.expensetracker.dto.expense.ExpenseRequest;
import com.project.expensetracker.dto.expense.ExpenseResponse;
import com.project.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    private static final Long FAKE_USER_ID = 1L;

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> getFiltered(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @PageableDefault(size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        ExpenseFilter filter = new ExpenseFilter(startDate, endDate, categoryId, minAmount, maxAmount);
        return ResponseEntity.ok(expenseService.getFiltered(FAKE_USER_ID, filter, pageable));
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> create(@Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = expenseService.create(FAKE_USER_ID, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.update(FAKE_USER_ID, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseService.delete(FAKE_USER_ID, id);
        return ResponseEntity.noContent().build();
    }
}