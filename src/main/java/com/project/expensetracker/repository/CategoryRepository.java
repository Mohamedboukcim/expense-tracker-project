package com.project.expensetracker.repository;

import com.project.expensetracker.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserId(Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
}