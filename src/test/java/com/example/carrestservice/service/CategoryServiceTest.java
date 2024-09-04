package com.example.carrestservice.service;

import com.example.carrestservice.entity.Category;
import com.example.carrestservice.exception.CategoryNameException;
import com.example.carrestservice.exception.CategoryNotFoundException;
import com.example.carrestservice.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    static Category category;

    @BeforeAll
    static void init() {
        category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("Test");
    }

    static Stream<Pageable> pageableProvider() {
        return Stream.of(
                PageRequest.of(0, 5),
                PageRequest.of(1, 10),
                PageRequest.of(2, 20),
                PageRequest.of(3, 25),
                PageRequest.of(4, 30),
                PageRequest.of(5, 40),
                PageRequest.of(6, 45),
                PageRequest.of(7, 50),
                PageRequest.of(8, 55),
                PageRequest.of(9, 60)
        );
    }

    @Test
    void createCategory_shouldReturnCategory_whenInputContainsCategory() {

        when(categoryRepository.existsByCategoryName(category.getCategoryName()))
                .thenReturn(false);

        when(categoryRepository.save(category))
                .thenReturn(category);

        Category actualCategory = categoryService.createCategory(category);

        assertNotNull(actualCategory);
        assertEquals(category, actualCategory);

        verify(categoryRepository).existsByCategoryName(category.getCategoryName());
        verify(categoryRepository).save(category);
    }

    @Test
    void createCategory_shouldThrowException_whenInputContainsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(null));

        assertEquals("Category cannot be null!", exception.getMessage());

        verify(categoryRepository, never()).existsByCategoryName(null);
        verify(categoryRepository, never()).save(null);
    }

    @Test
    void createCategory_shouldThrowException_whenInputContainsCategoryWithExistingName() {

        when(categoryRepository.existsByCategoryName(category.getCategoryName()))
                .thenReturn(true);

        CategoryNameException exception = assertThrows(CategoryNameException.class, () -> categoryService.createCategory(category));

        assertEquals("Category name " + category.getCategoryName() + " already exists!", exception.getMessage());

        verify(categoryRepository).existsByCategoryName(category.getCategoryName());
        verify(categoryRepository, never()).save(category);
    }

    @Test
    void updateCategory_shouldUpdateCategory_whenInputContainsCategory() {

        when(categoryRepository.findById(category.getCategoryId()))
                .thenReturn(Optional.of(category));

        when(categoryRepository.existsByCategoryName(category.getCategoryName()))
                .thenReturn(false);

        categoryService.updateCategory(category);

        verify(categoryRepository).findById(category.getCategoryId());
        verify(categoryRepository).existsByCategoryName(category.getCategoryName());
    }

    @Test
    void updateCategory_shouldThrowException_whenInputContainsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(null));
        assertEquals("Category cannot be null!", exception.getMessage());

        verify(categoryRepository, never()).findById(0L);
        verify(categoryRepository, never()).save(null);
    }

    @Test
    void updateCategory_shouldThrowException_whenInputContainsCategoryWithExistingName() {

        when(categoryRepository.findById(category.getCategoryId()))
                .thenReturn(Optional.ofNullable(category));

        when(categoryRepository.existsByCategoryName(category.getCategoryName()))
                .thenReturn(true);

        CategoryNameException exception = assertThrows(CategoryNameException.class, () -> categoryService.updateCategory(category));

        assertEquals("Category name " + category.getCategoryName() + " already exists!", exception.getMessage());

        verify(categoryRepository).findById(category.getCategoryId());
        verify(categoryRepository).existsByCategoryName(category.getCategoryName());
    }

    @Test
    void updateCategory_shouldThrowException_whenInputContainsCategoryWithNotExistingId() {

        when(categoryRepository.findById(category.getCategoryId()))
                .thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(category));

        assertEquals("Category with Id " + category.getCategoryId() + " not found.", exception.getMessage());

        verify(categoryRepository).findById(category.getCategoryId());
        verify(categoryRepository, never()).existsByCategoryName(null);
        verify(categoryRepository, never()).save(null);
    }

    @Test
    void removeById_shouldRemoveCategory_whenInputContainsExistingCategoryId() {

        long categoryId = 1L;

        when(categoryRepository.existsByCategoryId(categoryId))
                .thenReturn(true);

        doNothing().when(categoryRepository).deleteById(categoryId);

        categoryService.removeById(categoryId);

        verify(categoryRepository).existsByCategoryId(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void removeById_shouldThrowException_whenInputContainsNotExistingCategoryId(long categoryId) {

        when(categoryRepository.existsByCategoryId(categoryId))
                .thenReturn(false);

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.removeById(categoryId));

        assertEquals("Category with Id " + categoryId + " not found.", exception.getMessage());

        verify(categoryRepository).existsByCategoryId(categoryId);
        verify(categoryRepository, never()).deleteById(categoryId);
    }

    @ParameterizedTest
    @MethodSource("pageableProvider")
    void getAll_shouldReturnCategoryPage_whenInputContainsCorrectPageable(Pageable pageable) {

        Page<Category> page = new PageImpl<>(List.of(category), pageable, 2);

        when(categoryRepository.findAll(pageable))
                .thenReturn(page);

        Page<Category> actualPage = categoryService.getAll(pageable);

        assertEquals(page, actualPage);

        verify(categoryRepository).findAll(pageable);
    }

    @Test
    void getById_shouldReturnCategory_whenInputContainsExistingCategoryId() {

        long categoryId = 1L;

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.ofNullable(category));

        Category actualCategory = categoryService.getById(categoryId);

        assertNotNull(actualCategory);
        assertEquals(category, actualCategory);

        verify(categoryRepository).findById(categoryId);
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void getById_shouldThrowException_whenInputContainsNotExistingCategoryId(long categoryId) {

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.getById(categoryId));

        assertEquals("Category with Id " + categoryId + " not found.", exception.getMessage());

        verify(categoryRepository).findById(categoryId);
    }
}
