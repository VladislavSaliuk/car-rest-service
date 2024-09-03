package com.example.carrestservice.repository;

import com.example.carrestservice.entity.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Optional;

@Testcontainers
@SpringBootTest
@Sql(scripts = {"/sql/drop_data.sql","/sql/insert_categories.sql"})
public class CategoryRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void save_shouldSaveChanges() {
        Category category = new Category();
        categoryRepository.save(category);
        Assertions.assertEquals(11, categoryRepository.count());
    }

    @Test
    void findAll_shouldReturnCategoryList() {
        List<Category> categoryList = categoryRepository.findAll();
        Assertions.assertFalse(categoryList.isEmpty());
        Assertions.assertEquals(10, categoryList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void deleteById_shouldDeleteCategoryFromDatabase_whenInputContainsExistingCategoryId(long categoryId) {
        categoryRepository.deleteById(categoryId);
        Assertions.assertEquals(9, categoryRepository.count());
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void deleteById_shouldNotDeleteCategoryFromDatabase_whenInputContainsNotExistingCategoryId(long  categoryId) {
        categoryRepository.deleteById(categoryId);
        Assertions.assertEquals(10, categoryRepository.count());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void findById_shouldReturnCategory_whenInputContainsExistingCategoryId(long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Assertions.assertTrue(optionalCategory.isPresent());
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void findById_shouldReturnNull_whenInputContainsNotExistingCategoryId(long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Assertions.assertTrue(optionalCategory.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Sedan", "Hatchback", "Crossover", "SUV", "Coupe", "Convertible", "Minivan", "Pickup Truck", "Electric Car", "Sports Car"})
    void findByCategoryName_shouldReturnCategory_whenInputContainsExistingCategoryName(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
        Assertions.assertTrue(optionalCategory.isPresent());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test 1", "Test 2", "Test 3", "Test 4", "Test 5", "Test 6", "Test 7", "Test 8", "Test 9", "Test 10"})
    void findByCategoryName_shouldReturnNull_whenInputContainsNotExistingCategoryName(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
        Assertions.assertTrue(optionalCategory.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findByCategoryName_shouldReturnNull_whenInputContainsNullOrEmptyString(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
        Assertions.assertTrue(optionalCategory.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Sedan", "Hatchback", "Crossover", "SUV", "Coupe", "Convertible", "Minivan", "Pickup Truck", "Electric Car", "Sports Car"})
    void existsByCategoryName_shouldReturnTrue_whenInputContainsExistingCategoryName(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
        Assertions.assertTrue(optionalCategory.isPresent());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test 1", "Test 2", "Test 3", "Test 4", "Test 5", "Test 6", "Test 7", "Test 8", "Test 9", "Test 10"})
    void existsByCategoryName_shouldReturnFalse_whenInputContainsNotExistingCategoryName(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
        Assertions.assertTrue(optionalCategory.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void existsByCategoryName_shouldReturnFalse_whenInputContainsNull(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
        Assertions.assertTrue(optionalCategory.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void existsByCategoryId_shouldReturnTrue_whenInputContainsExistingCategoryId(long categoryId) {
        Assertions.assertTrue(categoryRepository.existsByCategoryId(categoryId));
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void existsByCategoryId_shouldReturnFalse_whenInputContainsNotExistingCategoryId(long categoryId) {
        Assertions.assertFalse(categoryRepository.existsByCategoryId(categoryId));
    }


}
