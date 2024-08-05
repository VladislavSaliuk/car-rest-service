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

    @PostMapping("/categories/create")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            categoryService.createCategory(category);
            return new ResponseEntity<>(category, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping("/categories/update")
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        try {
            categoryService.updateCategory(category);
            return new ResponseEntity<>(category,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/categories/remove/{id}")
    public ResponseEntity<?> removeCategoryById(@PathVariable("id") Long categoryId) {
        try {
            Category category = categoryService.removeById(categoryId);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        List<Category> categoryList = categoryService.getAll();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @GetMapping("/categories/sort")
    public ResponseEntity<?> getSortedCategories(@RequestParam(required = false, defaultValue = "categoryId") String sortField,
                                                @RequestParam(required = false, defaultValue = "DESC") String sortDirection){
        try {
            List<Category> categoryList = categoryService.getAll(sortDirection, sortField);
            return new ResponseEntity<>(categoryList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/categories/pagination")
    public ResponseEntity<?> getCategoryPage(
            @RequestParam int offset,
            @RequestParam int pageSize) {

        try {
            Page<Category> categoryPage = categoryService.getPage(offset, pageSize);
            return new ResponseEntity<>(categoryPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long categoryId) {
        try {
            Category category = categoryService.getById(categoryId);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
