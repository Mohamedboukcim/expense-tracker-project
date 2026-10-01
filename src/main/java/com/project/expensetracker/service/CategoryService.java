package com.project.expensetracker.service;

import com.project.expensetracker.dto.category.CategoryRequest;
import com.project.expensetracker.dto.category.CategoryResponse;
import com.project.expensetracker.entity.Category;
import com.project.expensetracker.entity.User;
import com.project.expensetracker.exception.ResourceNotFoundException;
import com.project.expensetracker.mapper.CategoryMapper;
import com.project.expensetracker.repository.CategoryRepository;
import com.project.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllForUser(Long userId) {
        return categoryRepository.findByUserId(userId)
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse create(Long userId, CategoryRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        Category category = Category.builder()
                .name(request.name())
                .user(user)
                .build();

        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    public CategoryResponse update(Long userId, Long categoryId, CategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable"));

        if (!category.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Catégorie introuvable");
        }

        category.setName(request.name());
        return categoryMapper.toResponse(category);
    }

    public void delete(Long userId, Long categoryId) {
        if (!categoryRepository.existsByIdAndUserId(categoryId, userId)) {
            throw new ResourceNotFoundException("Catégorie introuvable");
        }
        categoryRepository.deleteById(categoryId);
    }
}