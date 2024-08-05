package com.example.carrestservice.service;

import com.example.carrestservice.entity.Category;
import com.example.carrestservice.exception.CategoryNameException;
import com.example.carrestservice.exception.CategoryNotFoundException;
import com.example.carrestservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Category> getAll(String sortDirection, String sortField) {

        if(!isValidSortField(sortField)) {
            throw new IllegalArgumentException("Invalid sort field : " + sortField);
        }

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Sort sort = Sort.by(direction, sortField);
        return categoryRepository.findAll(sort);
    }

    public Page<Category> getPage(int offset, int pageSize) {

        if (offset < 0) {
            throw new IllegalArgumentException("Offset must be a non-negative integer.");
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be a positive integer.");
        }

        Pageable pageable = PageRequest.of(offset, pageSize);
        return categoryRepository.findAll(pageable);
    }

    private boolean isValidSortField(String sortField) {

        Field[] fields = Category.class.getDeclaredFields();

        List<String> fieldList = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());

        return fieldList.contains(sortField);
    }
    public Category getById(long categoryId) {

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with Id " + categoryId + " not found."));

    }

}
