package com.example.carrestservice.repository;

import com.example.carrestservice.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {

    boolean existsByManufacturerName(String manufacturerName);
    Optional<Manufacturer> findByManufacturerName(String manufacturerName);

}
