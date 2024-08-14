package com.example.carrestservice.repository;


import com.example.carrestservice.entity.Manufacturer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Sql(scripts = {"/sql/drop_data.sql","/sql/insert_manufacturers.sql"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ManufacturerRepositoryTest {

    @Autowired
    ManufacturerRepository manufacturerRepository;
    @Test
    void save_shouldSaveChanges() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturerRepository.save(manufacturer);
        Assertions.assertEquals(11, manufacturerRepository.count());
    }

    @Test
    void findAll_shouldReturnManufacturerList() {
        List<Manufacturer> manufacturerList = manufacturerRepository.findAll();
        Assertions.assertFalse(manufacturerList.isEmpty());
        Assertions.assertEquals(10, manufacturerList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void deleteById_shouldDeleteManufacturerFromDatabase_whenInputContainsExistingManufacturerId(long manufacturerId) {
        manufacturerRepository.deleteById(manufacturerId);
        Assertions.assertEquals(9, manufacturerRepository.count());
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void deleteById_shouldNotDeleteManufacturerFromDatabase_whenInputContainsNotExistingManufacturerId(long manufacturerId) {
        manufacturerRepository.deleteById(manufacturerId);
        Assertions.assertEquals(10, manufacturerRepository.count());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void findById_shouldReturnManufacturer_whenInputContainsExistingManufacturerId(long manufacturerId) {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findById(manufacturerId);
        Assertions.assertTrue(optionalManufacturer.isPresent());
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void findById_shouldReturnNull_whenInputContainsNotExistingManufacturerId(long manufacturerId) {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findById(manufacturerId);
        Assertions.assertTrue(optionalManufacturer.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Toyota", "Ford", "Honda", "BMW", "Mercedes-Benz", "Volkswagen", "Chevrolet", "Nissan", "Audi", "Hyundai"})
    void findByManufacturerName_shouldReturnManufacturer_whenInputContainsExistingManufacturerName(String manufacturerName) {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findByManufacturerName(manufacturerName);
        Assertions.assertTrue(optionalManufacturer.isPresent());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test 1", "Test 2", "Test 3", "Test 4", "Test 5", "Test 6", "Test 7", "Test 8", "Test 9", "Test 10"})    void findByManufacturerName_shouldReturnNull_whenInputContainsNotExistingManufacturerName(String manufacturerName) {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findByManufacturerName(manufacturerName);
        Assertions.assertTrue(optionalManufacturer.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findByManufacturerName_shouldReturnNull_whenInputContainsNullOrEmptyString(String manufacturerName) {
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findByManufacturerName(manufacturerName);
        Assertions.assertTrue(optionalManufacturer.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Toyota", "Ford", "Honda", "BMW", "Mercedes-Benz", "Volkswagen", "Chevrolet", "Nissan", "Audi", "Hyundai"})
    void existsByManufacturerName_shouldReturnTrue_whenInputContainsExistingManufacturerName(String manufacturerName) {
        Assertions.assertTrue(manufacturerRepository.existsByManufacturerName(manufacturerName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test 1", "Test 2", "Test 3", "Test 4", "Test 5", "Test 6", "Test 7", "Test 8", "Test 9", "Test 10"})    void existsByManufacturerName_shouldReturnFalse_whenInputContainsNotExistingManufacturerName(String manufacturerName) {
        Assertions.assertFalse(manufacturerRepository.existsByManufacturerName(manufacturerName));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void existsByManufacturerName_shouldReturnFalse_whenInputContainsNullOrEmptyString(String manufacturerName) {
        Assertions.assertFalse(manufacturerRepository.existsByManufacturerName(manufacturerName));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void existsByManufacturerId_shouldReturnTrue_whenInputContainsExistingManufacturerId(long manufacturerId) {
        Assertions.assertTrue(manufacturerRepository.existsByManufacturerId(manufacturerId));
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void existsByManufacturerId_shouldReturnFalse_whenInputContainsNotExistingManufacturerId(long manufacturerId) {
        Assertions.assertFalse(manufacturerRepository.existsByManufacturerId(manufacturerId));
    }


}
