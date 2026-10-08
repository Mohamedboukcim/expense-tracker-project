package com.project.expensetracker.service;

import com.project.expensetracker.dto.category.CategoryRequest;
import com.project.expensetracker.dto.category.CategoryResponse;
import com.project.expensetracker.entity.Category;
import com.project.expensetracker.entity.User;
import com.project.expensetracker.exception.ResourceNotFoundException;
import com.project.expensetracker.mapper.CategoryMapper;
import com.project.expensetracker.repository.CategoryRepository;
import com.project.expensetracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(1L).email("test@test.com").fullName("Test User").build();
        testCategory = Category.builder().id(10L).name("Alimentation").user(testUser).build();
    }

    @Test
    void create_shouldReturnCategoryResponse_whenUserExists() {
        CategoryRequest request = new CategoryRequest("Alimentation");
        CategoryResponse expectedResponse = new CategoryResponse(10L, "Alimentation");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);
        when(categoryMapper.toResponse(testCategory)).thenReturn(expectedResponse);

        CategoryResponse result = categoryService.create(1L, request);

        assertThat(result).isEqualTo(expectedResponse);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void create_shouldThrowException_whenUserDoesNotExist() {
        CategoryRequest request = new CategoryRequest("Alimentation");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.create(99L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Utilisateur introuvable");

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowException_whenCategoryBelongsToAnotherUser() {
        CategoryRequest request = new CategoryRequest("Nouveau nom");
        Long attackerUserId = 999L;

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(testCategory));

        assertThatThrownBy(() -> categoryService.update(attackerUserId, 10L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Catégorie introuvable");
    }

    @Test
    void delete_shouldCallRepository_whenCategoryBelongsToUser() {
        when(categoryRepository.existsByIdAndUserId(10L, 1L)).thenReturn(true);

        categoryService.delete(1L, 10L);

        verify(categoryRepository).deleteById(10L);
    }

    @Test
    void delete_shouldThrowException_whenCategoryDoesNotBelongToUser() {
        when(categoryRepository.existsByIdAndUserId(10L, 999L)).thenReturn(false);

        assertThatThrownBy(() -> categoryService.delete(999L, 10L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void getAllForUser_shouldReturnMappedList() {
        when(categoryRepository.findByUserId(1L)).thenReturn(List.of(testCategory));
        when(categoryMapper.toResponse(testCategory)).thenReturn(new CategoryResponse(10L, "Alimentation"));

        List<CategoryResponse> result = categoryService.getAllForUser(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Alimentation");
    }
}