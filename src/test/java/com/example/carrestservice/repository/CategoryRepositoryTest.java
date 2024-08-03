package com.example.carrestservice.repository;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_categories.sql"})
    void save_shouldSaveChanges() {
        Category category = new Category("Test category");
        categoryRepository.save(category);
        assertEquals(11, categoryRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_categories.sql"})
    void findAll_shouldReturnCategoryList() {
        List<Category> categoryList = categoryRepository.findAll();
        assertFalse(categoryList.isEmpty());
        assertEquals(10, categoryList.size());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_categories.sql"})
    void deleteById_shouldDeleteCategoryFromDatabase_whenInputContainsExistingCategoryId() {
        long categoryId = 5;
        categoryRepository.deleteById(categoryId);
        assertEquals(9, categoryRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_categories.sql"})
    void deleteById_shouldNotDeleteCategoryFromDatabase_whenInputContainsNotExistingCategoryId() {
        long categoryId =100;
        categoryRepository.deleteById(categoryId);
        assertEquals(10, categoryRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_categories.sql"})
    void findById_shouldReturnCategory_whenInputContainsExistingCategoryId() {

        long categoryId = 3;
        String categoryName = "Crossover";

        Category expectedCategory = new Category();

        expectedCategory.setCategoryId(categoryId);
        expectedCategory.setCategoryName(categoryName);
        expectedCategory.setCars(Collections.emptySet());

        Optional<Category> actualCategory = categoryRepository.findById(categoryId);

        assertTrue(actualCategory.isPresent());
        assertEquals(expectedCategory, actualCategory.get());

    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_categories.sql"})
    void findById_shouldReturnNull_whenInputContainsNotExistingCategoryId() {
        long categoryId = 100;
        Optional<Category> expectedCategory = categoryRepository.findById(categoryId);
        assertTrue(expectedCategory.isEmpty());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_categories.sql"})
    void findByCategoryName_shouldReturnCategory_whenInputContainsExistingCategoryName() {

        long categoryId = 3;
        String categoryName = "Crossover";

        Category expectedCategory = new Category();

        expectedCategory.setCategoryId(categoryId);
        expectedCategory.setCategoryName(categoryName);
        expectedCategory.setCars(Collections.emptySet());

        Optional<Category> actualCategory = categoryRepository.findByCategoryName(categoryName);

        assertTrue(actualCategory.isPresent());
        assertEquals(expectedCategory, actualCategory.get());

    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_categories.sql"})
    void findByCategoryName_shouldReturnNull_whenInputContainsNotExistingCategoryName() {
        String categoryName = "Test category name";
        Optional<Category> expectedCategory = categoryRepository.findByCategoryName(categoryName);
        assertTrue(expectedCategory.isEmpty());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_categories.sql"})
    void findByCategoryName_shouldReturnNull_whenInputContainsNull() {
        Optional<Category> expectedCategory = categoryRepository.findByCategoryName(null);
        assertTrue(expectedCategory.isEmpty());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_categories.sql"})
    void existsByCategoryName_shouldReturnTrue_whenInputContainsExistingCategoryName() {
        String categoryName = "Crossover";
        Optional<Category> expectedCategory = categoryRepository.findByCategoryName(categoryName);
        assertTrue(expectedCategory.isPresent());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_categories.sql"})
    void existsByCategoryName_shouldReturnFalse_whenInputContainsNotExistingCategoryName() {
        String categoryName = "Test category name";
        Optional<Category> expectedCategory = categoryRepository.findByCategoryName(categoryName);
        assertTrue(expectedCategory.isEmpty());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql","/sql/insert_categories.sql"})
    void existsByCategoryName_shouldReturnFalse_whenInputContainsNull() {
        Optional<Category> expectedCategory = categoryRepository.findByCategoryName(null);
        assertTrue(expectedCategory.isEmpty());
    }


}
