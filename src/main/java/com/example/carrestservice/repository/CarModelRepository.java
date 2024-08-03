package com.example.carrestservice.repository;

import com.example.carrestservice.entity.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    Optional<CarModel> findByCarModelName(String carModelName);
    boolean existsByCarModelName(String carModelName);

}
