package com.example.carrestservice.service;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Category;
import com.example.carrestservice.exception.CategoryNameException;
import com.example.carrestservice.exception.CategoryNotFoundException;
import com.example.carrestservice.repository.CategoryRepository;
import jakarta.persistence.Column;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;


    @Test
    void createCategory_shouldReturnCategory_whenInputContainsCategory() {

        String categoryName = "Test category name";

        Category expectedCategory = new Category();

        expectedCategory.setCategoryName(categoryName);
        expectedCategory.setCars(Collections.emptySet());

        when(categoryRepository.findByCategoryName(categoryName))
                .thenReturn(Optional.empty());

        when(categoryRepository.save(expectedCategory))
                .thenReturn(expectedCategory);

        Category actualCategory = categoryService.createCategory(expectedCategory);

        assertNotNull(actualCategory);
        assertEquals(expectedCategory, actualCategory);

        verify(categoryRepository).findByCategoryName(categoryName);
        verify(categoryRepository).save(expectedCategory);

    }

    @Test
    void createCategory_shouldThrowException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(null));
        assertEquals("Category can not be null!", exception.getMessage());

        verify(categoryRepository, never()).findByCategoryName(null);
        verify(categoryRepository, never()).save(null);
    }

    @Test
    void createCategory_shouldThrowException_whenInputContainsCategoryWithExistingCategoryName() {

        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setCars(Collections.emptySet());

        when(categoryRepository.findByCategoryName(categoryName))
                .thenReturn(Optional.ofNullable(category));

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.createCategory(category));

        assertEquals("Category name " + category.getCategoryName() + " already exists!", exception.getMessage());

        verify(categoryRepository).findByCategoryName(categoryName);
        verify(categoryRepository, never()).save(category);

    }

    @Test
    void updateCategory_shouldReturnCategory_whenInputContainsCategory() {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();

        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setCars(Collections.emptySet());

        Category updatedCategory = new Category();

        updatedCategory.setCategoryId(categoryId);
        updatedCategory.setCategoryName("Test category 1");
        updatedCategory.setCars(Collections.emptySet());

        when(categoryRepository.findById(category.getCategoryId()))
                .thenReturn(Optional.ofNullable(category));

        when(categoryRepository.existsByCategoryName(updatedCategory.getCategoryName()))
                .thenReturn(false);

        when(categoryRepository.save(updatedCategory))
                .thenReturn(updatedCategory);

        Category actualCategory = categoryService.updateCategory(updatedCategory);

        assertNotNull(actualCategory);
        assertEquals(updatedCategory, actualCategory);

        verify(categoryRepository).findById(category.getCategoryId());
        verify(categoryRepository).existsByCategoryName(updatedCategory.getCategoryName());
        verify(categoryRepository).save(updatedCategory);

    }


    @Test
    void updateCategory_shouldThrowException_whenInputContainsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> categoryService.updateCategory(null));
        assertEquals("Category can not be null!", exception.getMessage());

        verify(categoryRepository,never()).findById(0L);
        verify(categoryRepository,never()).existsByCategoryName(null);
        verify(categoryRepository,never()).save(null);

    }

    @Test
    void updateCategory_shouldThrowException_whenInputContainsCategoryWithExistingCategoryName() {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();

        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setCars(Collections.emptySet());

        Category updatedCategory = new Category();

        updatedCategory.setCategoryId(categoryId);
        updatedCategory.setCategoryName(categoryName);
        updatedCategory.setCars(Collections.emptySet());

        when(categoryRepository.findById(category.getCategoryId()))
                .thenReturn(Optional.ofNullable(category));

        when(categoryRepository.existsByCategoryName(updatedCategory.getCategoryName()))
                .thenReturn(true);

        CategoryNameException exception = assertThrows(CategoryNameException.class, () -> categoryService.updateCategory(updatedCategory));

        assertEquals("Category name " + updatedCategory.getCategoryName() + " already exists!", exception.getMessage());

        verify(categoryRepository).findById(category.getCategoryId());
        verify(categoryRepository).existsByCategoryName(updatedCategory.getCategoryName());
        verify(categoryRepository,never()).save(null);

    }

    @Test
    void updateCategory_shouldThrowException_whenInputContainsCategoryWithNotExistingCategoryId() {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();

        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setCars(Collections.emptySet());

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(category));

        assertEquals("Category with Id " + category.getCategoryId() + " not found.", exception.getMessage());

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository, never()).existsByCategoryName(null);
        verify(categoryRepository,never()).save(null);

    }

    @Test
    void removeById_shouldReturnCategory_whenInputContainsExistingCategoryId() {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category expectedCategory = new Category();

        expectedCategory.setCategoryId(categoryId);
        expectedCategory.setCategoryName(categoryName);
        expectedCategory.setCars(Collections.emptySet());

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.ofNullable(expectedCategory));

        doNothing().when(categoryRepository).deleteById(categoryId);

        Category actualCategory = categoryService.removeById(categoryId);

        assertNotNull(actualCategory);
        assertEquals(expectedCategory, actualCategory);

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).deleteById(categoryId);

    }

    @Test
    void removeById_shouldThrowException_whenInputContainsCategoryWithNotExistingCategoryId() {

        long categoryId = 100;

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.removeById(categoryId));

        assertEquals("Category with Id " + categoryId + " not found.", exception.getMessage());

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository,never()).deleteById(categoryId);

    }

    @Test
    void getAll_shouldReturnCorrectCategoryList() {

        when(categoryRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Category> categoryList = categoryService.getAll();

        assertTrue(categoryList.isEmpty());

        verify(categoryRepository).findAll();

    }

    @Test
    void getAll_shouldReturnCategoryList_whenInputContainsCorrectData() {

        String sortDirection = "ASC";
        String sortField = "categoryName";

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Sort sort = Sort.by(direction, sortField);

        when(categoryRepository.findAll(sort))
                .thenReturn(Collections.emptyList());

        List<Category> categoryList = categoryService.getAll();

        assertNotNull(categoryList);
        assertTrue(categoryList.isEmpty());

        verify(categoryRepository).findAll();

    }

    @Test
    void getAll_shouldThrowException_whenInputContainsNotExistingSortField() {

        String sortDirection = "ASC";
        String sortField = "Test sort field";


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> categoryService.getAll(sortDirection, sortField));

        assertEquals("Invalid sort field : " + sortField, exception.getMessage());

        verify(categoryRepository, never()).findAll();

    }

    @Test
    void getPage_shouldReturnPage_whenInputContainsCorrectData() {

        int offset = 10;
        int pageSize = 10;

        Pageable pageable = PageRequest.of(offset, pageSize);

        Page<Category> expectedCategoryPage = new PageImpl<>(Collections.emptyList(),pageable,0);

        when(categoryRepository.findAll(pageable))
                .thenReturn(expectedCategoryPage);

        Page<Category> actualCategoryPage = categoryService.getPage(offset, pageSize);

        assertNotNull(actualCategoryPage);
        assertEquals(expectedCategoryPage, actualCategoryPage);

        verify(categoryRepository).findAll(pageable);

    }

    @Test
    void getPage_shouldThrowException_whenInputContainsNegativeOffset() {

        int offset = -10;
        int pageSize = 10;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> categoryService.getPage(offset, pageSize));

        assertEquals("Offset must be a non-negative integer.", exception.getMessage());

        verify(categoryRepository, never()).findAll();

    }

    @Test
    void getPage_shouldThrowException_whenInputContainsNegativePageSize() {

        int offset = 10;
        int pageSize = -10;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> categoryService.getPage(offset, pageSize));

        assertEquals("Page size must be a positive integer.", exception.getMessage());

        verify(categoryRepository, never()).findAll();

    }

    @Test
    void getById_shouldReturnCategory_whenInputContainsExistingCategoryId() {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category expectedCategory = new Category();

        expectedCategory.setCategoryId(categoryId);
        expectedCategory.setCategoryName(categoryName);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.ofNullable(expectedCategory));

        Category actualCategory = categoryService.getById(categoryId);

        assertNotNull(actualCategory);
        assertEquals(expectedCategory, actualCategory);

        verify(categoryRepository).findById(categoryId);

    }

    @Test
    void getById_shouldThrowException_whenInputContainsCategoryWithNotExistingCategoryId() {

        long categoryId = 100;

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.getById(categoryId));

        assertEquals("Category with Id " + categoryId + " not found." , exception.getMessage());

        verify(categoryRepository).findById(categoryId);

    }

}
