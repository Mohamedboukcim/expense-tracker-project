package com.project.expensetracker.mapper;

import com.project.expensetracker.dto.category.CategoryResponse;
import com.project.expensetracker.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}