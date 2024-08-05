package com.example.carrestservice.rest;


import com.example.carrestservice.entity.Category;
import com.example.carrestservice.exception.CategoryNameException;
import com.example.carrestservice.exception.CategoryNotFoundException;
import com.example.carrestservice.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryRestController.class)
public class CategoryRestControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CategoryService categoryService;


    @Test
    void createCategory_shouldReturnCreatedRequest_whenInputContainsCorrectData() throws Exception {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryService.createCategory(category))
                .thenReturn(category);

        mockMvc.perform(post("/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value(category.getCategoryId()));

        verify(categoryService).createCategory(category);
    }

    @Test
    void createCategory_shouldReturnBadRequest_whenInputContainsNull() throws Exception {

        Category category = new Category();

        when(categoryService.createCategory(any(Category.class)))
                .thenThrow(new IllegalArgumentException("Category can not be null!"));

        mockMvc.perform(post("/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Category can not be null!"));

        verify(categoryService).createCategory(any(Category.class));
    }

    @Test
    void createCategory_shouldReturnBadRequest_whenInputContainsAlreadyExistingCategoryName() throws Exception {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryService.createCategory(category))
                .thenThrow(new CategoryNameException("Category name " + category.getCategoryName() + " already exists!"));

        mockMvc.perform(post("/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Category name " + category.getCategoryName() + " already exists!"));

        verify(categoryService).createCategory(category);

    }

    @Test
    void updateCategory_shouldReturnCreatedRequest_whenInputContainsCorrectData() throws Exception {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryService.updateCategory(category))
                .thenReturn(category);

        mockMvc.perform(put("/categories/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(category.getCategoryId()));

        verify(categoryService).updateCategory(category);

    }

    @Test
    void updateCategory_shouldReturnBadRequest_whenInputContainsNull() throws Exception {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryService.updateCategory(category))
                .thenThrow(new CategoryNameException("Category name " + category.getCategoryName() + " already exists!"));

        mockMvc.perform(put("/categories/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Category name " + category.getCategoryName() + " already exists!"));

        verify(categoryService).updateCategory(category);

    }

    @Test
    void updateCategory_shouldReturnBadRequest_whenInputContainsCategoryWithNotExistingCategoryId() throws Exception {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryService.updateCategory(category))
                .thenThrow(new CategoryNameException("Category name " + category.getCategoryName() + " already exists!"));

        mockMvc.perform(put("/categories/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Category name " + category.getCategoryName() + " already exists!"));

        verify(categoryService).updateCategory(category);

    }

    @Test
    void updateCategory_shouldReturnBadRequest_whenInputAlreadyExistingCategoryName() throws Exception {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryService.updateCategory(category))
                .thenThrow(new CategoryNotFoundException("Category with Id " + category.getCategoryId() + " not found."));

        mockMvc.perform(put("/categories/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Category with Id " + category.getCategoryId() + " not found."));

        verify(categoryService).updateCategory(category);

    }

    @Test
    void removeCategoryById_shouldReturnOkRequest_whenInputContainsExistingCategoryId() throws Exception {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryService.removeById(categoryId))
                .thenReturn(category);

        mockMvc.perform(delete("/categories/remove/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(category.getCategoryId()));

        verify(categoryService).removeById(categoryId);

    }

    @Test
    void removeCategoryById_shouldReturnBadRequest_whenInputContainsNotExistingCategoryId() throws Exception {

        long categoryId = 100;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryService.removeById(categoryId))
                .thenThrow(new CategoryNotFoundException("Category with Id " + categoryId + " not found."));

        mockMvc.perform(delete("/categories/remove/100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Category with Id " + category.getCategoryId() + " not found."));

        verify(categoryService).removeById(categoryId);

    }

    @Test
    void getCategories_shouldReturnOkRequest() throws Exception {

        when(categoryService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(categoryService).getAll();

    }

    @Test
    void getSortedCategories_shouldReturnOkRequest_whenInputContainsCorrectData() throws Exception {

        String sortField = "categoryName";
        String sortDirection = "ASC";

        when(categoryService.getAll(sortDirection, sortField))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/categories/sort")
                .param("sortField", sortField)
                .param("sortDirection", sortDirection))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(categoryService).getAll(sortDirection, sortField);
    }
    @Test
    void getSortedCategories_shouldReturnOkRequest_whenInputContainsInCorrectSortField() throws Exception {

        String sortField = "test category field";
        String sortDirection = "ASC";

        when(categoryService.getAll(sortDirection, sortField))
                .thenThrow(new IllegalArgumentException("Invalid sort field : " + sortField));

        mockMvc.perform(get("/categories/sort")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDirection))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Invalid sort field : " + sortField));

        verify(categoryService).getAll(sortDirection, sortField);
    }


    @Test
    void getCategoryPage_shouldReturnOkRequest_whenInputContainsCorrectData() throws Exception {

        int offset = 10;
        int pageSize = 10;

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        Page<Category> categoryPage = new PageImpl<>(List.of(category));

        when(categoryService.getPage(offset, pageSize))
                .thenReturn(categoryPage);

        mockMvc.perform(get("/categories/pagination")
                        .param("offset", String.valueOf(offset))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryId").value(category.getCategoryId()));

        verify(categoryService).getPage(offset, pageSize);

    }

    @Test
    void getCategoryPage_shouldReturnBadRequestRequest_whenInputContainsNegativeOffset() throws Exception {

        int offset = -10;
        int pageSize = 10;

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryService.getPage(offset, pageSize))
                .thenThrow(new IllegalArgumentException("Offset must be a non-negative integer."));

        mockMvc.perform(get("/categories/pagination")
                        .param("offset", String.valueOf(offset))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Offset must be a non-negative integer."));

        verify(categoryService).getPage(offset, pageSize);

    }

    @Test
    void getCategoryPage_shouldReturnBadRequestRequest_whenInputContainsNegativePageSize() throws Exception {

        int offset = 10;
        int pageSize = -10;

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryService.getPage(offset, pageSize))
                .thenThrow(new IllegalArgumentException("Page size must be a positive integer."));

        mockMvc.perform(get("/categories/pagination")
                        .param("offset", String.valueOf(offset))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Page size must be a positive integer."));

        verify(categoryService).getPage(offset, pageSize);

    }

    @Test
    void getCategoryById_shouldReturnOkRequest_whenInputContainsExistingCategoryId() throws Exception {

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryService.getById(categoryId))
                .thenReturn(category);

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryId").value(category.getCategoryId()));

        verify(categoryService).getById(categoryId);

    }

    @Test
    void getCategoryById_shouldReturnBadRequest_whenInputContainsNotExistingCategoryId() throws Exception {

        long categoryId = 100;
        String categoryName = "Test category name";

        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);

        when(categoryService.getById(categoryId))
                .thenThrow(new CategoryNotFoundException("Category with Id " + categoryId + " not found."));

        mockMvc.perform(get("/categories/100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Category with Id " + categoryId + " not found."));

        verify(categoryService).getById(categoryId);

    }



}
