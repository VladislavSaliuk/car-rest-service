package com.example.carrestservice.service;

import com.example.carrestservice.entity.Category;
import com.example.carrestservice.exception.CategoryNameException;
import com.example.carrestservice.exception.CategoryNotFoundException;
import com.example.carrestservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    public Category createCategory(Category category) {

        if(category == null) {
            throw new IllegalArgumentException("Category can not be null!");
        }

        Optional<Category> existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());

        if(existingCategory.isPresent()) {
            throw new CategoryNotFoundException("Category name " + category.getCategoryName() + " already exists!");
        }

        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category) {

        if(category == null) {
            throw new IllegalArgumentException("Category can not be null!");
        }

        Optional<Category> optionalCategory = categoryRepository.findById(category.getCategoryId());

        return optionalCategory.map(updatedCategory -> {

            updatedCategory.setCategoryName(category.getCategoryName());

            boolean isCategoryNameTaken = categoryRepository.existsByCategoryName(updatedCategory.getCategoryName());

            if (isCategoryNameTaken) {
                throw new CategoryNameException("Category name " + updatedCategory.getCategoryName() + " already exists!");
            }

            return categoryRepository.save(updatedCategory);

        }).orElseThrow(() ->
                new CategoryNotFoundException("Category with Id " + category.getCategoryId() + " not found.")
        );

    }


    public Category removeById(long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with Id " + categoryId + " not found."));

        categoryRepository.deleteById(categoryId);

        return category;
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(long categoryId) {

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with Id " + categoryId + " not found."));

    }

}
