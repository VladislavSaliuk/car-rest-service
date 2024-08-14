package com.example.carrestservice.repository;

import com.example.carrestservice.entity.CarModel;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Sql(scripts = {"/sql/drop_data.sql","/sql/insert_car_models.sql"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarModelRepositoryTest {

    @Autowired
    CarModelRepository carModelRepository;

    @Test
    void save_shouldSaveChanges_whenInputContainsCarModelWithNotExistingCarModelName() {
        CarModel carModel = new CarModel();
        carModelRepository.save(carModel);
        assertEquals(11, carModelRepository.count());
    }
    @Test
    void findAll_shouldReturnCarModelList() {
        List<CarModel> carModelList = carModelRepository.findAll();
        assertFalse(carModelList.isEmpty());
        assertEquals(10, carModelList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void deleteById_shouldDeleteCarModelFromDatabase_whenInputContainsExistingCarModelId(long carModelId) {
        carModelRepository.deleteById(carModelId);
        assertEquals(9, carModelRepository.count());
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void deleteById_shouldNotDeleteCarModelFromDatabase_whenInputContainsNotExistingCarModelId(long carModelId) {
        carModelRepository.deleteById(carModelId);
        assertEquals(10, carModelRepository.count());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void findById_shouldReturnCarModel_whenInputContainsExistingCarModelId(long carModelId) {
        Optional<CarModel> optionalCarModel = carModelRepository.findById(carModelId);
        assertTrue(optionalCarModel.isPresent());
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void findById_shouldReturnNull_whenInputContainsNotExistingCarModelId(long carModelId) {
        Optional<CarModel> optionalCarModel = carModelRepository.findById(carModelId);
        assertTrue(optionalCarModel.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Camry", "Civic", "Mustang", "Accord", "Model S", "3 Series", "A4", "Corolla", "F-150", "Explorer"})
    void findByCarModelName_shouldReturnCarModel_whenInputContainsExistingCarModelName(String carModelName) {
        Optional<CarModel> optionalCarModel = carModelRepository.findByCarModelName(carModelName);
        assertTrue(optionalCarModel.isPresent());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test 1", "Test 2", "Test 3", "Test 4", "Test 5", "Test 6", "Test 7", "Test 8", "Test 9", "Test 10"})
    void findByCarModelName_shouldReturnNull_whenInputContainsNotExistingCarModelName(String carModelName) {
        Optional<CarModel> optionalCarModel = carModelRepository.findByCarModelName(carModelName);
        assertTrue(optionalCarModel.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findByCarModelName_shouldReturnNull_whenInputContainsNullOrEmptyString(String carModelName) {
        Optional<CarModel> optionalCarModel = carModelRepository.findByCarModelName(null);
        assertTrue(optionalCarModel.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Camry", "Civic", "Mustang", "Accord", "Model S", "3 Series", "A4", "Corolla", "F-150", "Explorer"})
    void existsByCarModelName_shouldReturnTrue_whenInputContainsExistingCarModelName(String carModelName) {
        assertTrue(carModelRepository.existsByCarModelName(carModelName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test 1", "Test 2", "Test 3", "Test 4", "Test 5", "Test 6", "Test 7", "Test 8", "Test 9", "Test 10"})
    void existsByCarModelName_shouldReturnFalse_whenInputContainsNotExistingCarModelName(String carModelName) {
        assertFalse(carModelRepository.existsByCarModelName(carModelName));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void existsByCarModelName_shouldReturnFalse_whenInputContainsNull(String carModelName) {
        assertFalse(carModelRepository.existsByCarModelName(carModelName));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L})
    void existsByCarModelId_shouldReturnTrue_whenInputContainsExistingCarModelId(long carModelId) {
        assertTrue(carModelRepository.existsByCarModelId(carModelId));
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void existsByCarModelId_shouldReturnFalse_whenInputContainsNotExistingCarModelId(long carModelId) {
        assertFalse(carModelRepository.existsByCarModelId(carModelId));
    }



}
