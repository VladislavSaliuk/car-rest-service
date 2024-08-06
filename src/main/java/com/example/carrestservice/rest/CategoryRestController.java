package com.example.carrestservice.rest;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Category;
import com.example.carrestservice.exception.ApiError;
import com.example.carrestservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }



    @PutMapping("/categories")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) {
        categoryService.updateCategory(category);
        return new ResponseEntity<>(category,HttpStatus.OK);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Category> removeCategoryById(@PathVariable("id") Long categoryId) {
        Category category = categoryService.removeById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categoryList = categoryService.getAll();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @GetMapping("/categories/sort")
    public ResponseEntity<List<Category>> getSortedCategories(@RequestParam(required = false, defaultValue = "categoryId") String sortField,
                                                @RequestParam(required = false, defaultValue = "DESC") String sortDirection){
        List<Category> categoryList = categoryService.getAll(sortDirection, sortField);
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @GetMapping("/categories/pagination")
    public ResponseEntity<List<Category>> getCategoryPage(
            @RequestParam int offset,
            @RequestParam int pageSize) {

        Page<Category> categoryPage = categoryService.getPage(offset, pageSize);
        return new ResponseEntity<>(categoryPage.getContent(), HttpStatus.OK);
    }
    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long categoryId) {
            Category category = categoryService.getById(categoryId);
            return new ResponseEntity<>(category, HttpStatus.OK);
    }
}
