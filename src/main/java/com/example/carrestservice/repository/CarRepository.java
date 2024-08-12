package com.example.carrestservice.repository;

import com.example.carrestservice.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CarRepository extends JpaRepository<Car, Long> {

    boolean existsByCarModel_CarModelId(long carModelId);

    boolean existsByCarId(long carId);

}
