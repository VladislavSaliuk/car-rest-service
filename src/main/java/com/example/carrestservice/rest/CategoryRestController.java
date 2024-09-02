package com.example.carrestservice.rest;

import com.example.carrestservice.entity.Category;
import com.example.carrestservice.exception.ApiError;
import com.example.carrestservice.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "category" , description = "APIs for managing categories, including create, update, delete, and retrieve operations.")
@RestController
public class CategoryRestController {
    private CategoryService categoryService;
    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Operation(summary = "create a new category", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "category was created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class)) }),
            @ApiResponse(responseCode = "401", description = "category can not be null", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "409", description = "category with this name already exist", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))})
    })
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@Parameter(description = "category to be created") @RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @Operation(summary = "update existing category", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "category was updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))}),
            @ApiResponse(responseCode = "401", description = "category can not be null", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "409", description = "category with this name already exist", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))})
    })
    @PutMapping("/categories")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCategory(@Parameter(description = "category to be updated") @RequestBody Category category) {
        categoryService.updateCategory(category);
    }


    @Operation(summary = "remove category by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "category was removed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))}),
            @ApiResponse(responseCode = "404", description = "category with this id not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))})
    })
    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategoryById(@Parameter(description = "category id to be removed") @PathVariable("id") Long categoryId) {
        categoryService.removeById(categoryId);
    }


    @Operation(summary = "get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))}),
            @ApiResponse(responseCode = "401", description = "offset must be a non-negative integer", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))}),
            @ApiResponse(responseCode = "401", description = "page size must be a positive integer", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))}),
    })
    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public Page<Category> getCategories(
            @Parameter(description = "sort field")
            @RequestParam(required = false, defaultValue = "categoryId") String sortField,
            @Parameter(description = "sort direction")
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @Parameter(description = "offset")
            @RequestParam(required = false, defaultValue = "0") int offset,
            @Parameter(description = "page size")
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(offset, pageSize,sort);

        return categoryService.getAll(pageable);
    }


    @Operation(summary = "get category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "category was found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Category.class))}),
            @ApiResponse(responseCode = "404", description = "category with this id not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class))})
    })
    @GetMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Category getCategoryById(@Parameter(description = "category id to be searched") @PathVariable Long id) {
        return categoryService.getById(id);
    }
}
