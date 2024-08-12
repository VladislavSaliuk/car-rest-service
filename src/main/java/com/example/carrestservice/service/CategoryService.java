package com.example.carrestservice.service;

import com.example.carrestservice.entity.Category;
import com.example.carrestservice.exception.CategoryNameException;
import com.example.carrestservice.exception.CategoryNotFoundException;
import com.example.carrestservice.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {

        if(category == null) {
            throw new IllegalArgumentException("Category cannot be null!");
        }

        if(categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new CategoryNameException("Category name " + category.getCategoryName() + " already exists!");
        }

        return categoryRepository.save(category);
    }

    public void updateCategory(Category category) {

        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null!");
        }

        Category updatedCategory = categoryRepository.findById(category.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category with Id " + category.getCategoryId() + " not found."));


        Optional.of(updatedCategory.getCategoryName())
                .filter(categoryName -> !categoryRepository.existsByCategoryName(category.getCategoryName()))
                .ifPresentOrElse(
                        categoryName -> updatedCategory.setCategoryName(category.getCategoryName()),
                        () -> {
                            throw new CategoryNameException("Category name " + category.getCategoryName() + " already exists!");
                        }
                );

    }



    public void removeById(long categoryId) {

        if(!categoryRepository.existsByCategoryId(categoryId)) {
            throw new CategoryNotFoundException("Category with Id " + categoryId + " not found.");
        } else {
            categoryRepository.deleteById(categoryId);
        }

    }

    public Page<Category> getAll(Pageable pageable) {

        if (pageable.getOffset() < 0) {
            throw new IllegalArgumentException("Offset must be a non-negative integer.");
        }
        if (pageable.getPageSize() <= 0) {
            throw new IllegalArgumentException("Page size must be a positive integer.");
        }

        return categoryRepository.findAll(pageable);
    }
    public Category getById(long categoryId) {

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with Id " + categoryId + " not found."));

    }

}
