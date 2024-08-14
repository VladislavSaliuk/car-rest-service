package com.example.carrestservice.repository;

import com.example.carrestservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByCategoryName(String categoryName);
    Optional<Category> findByCategoryName(String categoryName);
    boolean existsByCategoryId(long categoryId);

}
