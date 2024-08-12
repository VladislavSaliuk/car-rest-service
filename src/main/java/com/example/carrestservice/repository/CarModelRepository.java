package com.example.carrestservice.repository;

import com.example.carrestservice.entity.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    Optional<CarModel> findByCarModelName(String carModelName);
    boolean existsByCarModelName(String carModelName);
    boolean existsByCarModelId(long carModelId);

}
