package com.example.carrestservice.repository;

import com.example.carrestservice.entity.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_car_models.sql", "/sql/insert_categories.sql", "/sql/insert_manufacturers.sql", "/sql/insert_cars.sql"})
public class CarRepositoryTest {
    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    CarRepository carRepository;
    @Test
    void save_shouldSaveChanges_whenInputContainsCorrectCar() {
        Car car = new Car();
        carRepository.save(car);
        assertEquals(10, carRepository.count());

    }
    @Test
    void findAll_shouldReturnCarList() {
        List<Car> carList = carRepository.findAll();
        assertEquals(9, carList.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L})
    void deleteById_shouldDeleteCarFriDatabase_whenInputContainsExistingCarId(long carId) {
        carRepository.deleteById(carId);
        assertEquals(8, carRepository.count());
    }
    @ParameterizedTest
    @ValueSource(longs = {20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void deleteById_shouldNotDeleteCarFromDatabase_whenInputContainsNotExistingCarId(long carId) {
        carRepository.deleteById(carId);
        assertEquals(9, carRepository.count());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L})
    void findById_shouldReturnCar_whenInputContainsExistingCarId(long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        assertTrue(optionalCar.isPresent());
    }

    @ParameterizedTest
    @ValueSource(longs = {20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void findById_shouldReturnNull_whenInputContainsNotExistingCarId(long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        assertTrue(optionalCar.isEmpty());
    }
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L})
    void existsByCarModel_CarModelId_shouldReturnTrue_whenInputContainsExistingCarModelId(long carModelId) {
        assertTrue(carRepository.existsByCarModel_CarModelId(carModelId));
    }

    @ParameterizedTest
    @ValueSource(longs = {20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void existsByCarModel_CarModelId_shouldReturnFalse_whenInputContainsNotExistingCarModelId(long carModelId) {
        assertFalse(carRepository.existsByCarModel_CarModelId(carModelId));
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L})
    void existsByCarId_shouldReturnTrue_whenInputContainsExistingCarId(long carId) {
        assertTrue(carRepository.existsByCarId(carId));
    }

    @ParameterizedTest
    @ValueSource(longs = {20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void existsByCarId_shouldReturnFalse_whenInputContainsNotExistingCarId(long carId) {
        assertFalse(carRepository.existsByCarId(carId));
    }

}
