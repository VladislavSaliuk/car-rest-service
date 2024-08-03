package com.example.carrestservice.repository;

import com.example.carrestservice.entity.Car;
import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Category;
import com.example.carrestservice.entity.Manufacturer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarRepositoryTest {

    @Autowired
    CarRepository carRepository;

    @Autowired
    CarModelRepository carModelRepository;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_car_models.sql", "/sql/insert_categories.sql", "/sql/insert_manufacturers.sql", "/sql/insert_cars.sql"})
    void save_shouldSaveChanges_whenInputContainsCorrectCar() {

        long carModelId = 10;
        long categoryId = 1;
        long manufacturerId = 1;

        Optional<CarModel> carModel = carModelRepository.findById(carModelId);
        Optional<Category> category = categoryRepository.findById(categoryId);
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(manufacturerId);

        Car car = new Car(manufacturer.get(), 2024, carModel.get(), category.get());

        carRepository.save(car);

        assertTrue(carModel.isPresent());
        assertTrue(category.isPresent());
        assertTrue(manufacturer.isPresent());

        assertEquals(10, carRepository.count());

    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_car_models.sql", "/sql/insert_categories.sql", "/sql/insert_manufacturers.sql", "/sql/insert_cars.sql"})
    void save_shouldThrowException_whenInputContainsCarWithAlreadyExistingCarModel() {

        long carModelId = 5;
        long categoryId = 1;
        long manufacturerId = 1;

        Optional<CarModel> carModel = carModelRepository.findById(carModelId);
        Optional<Category> category = categoryRepository.findById(categoryId);
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(manufacturerId);

        Car car = new Car(manufacturer.get(), 2024, carModel.get(), category.get());

        assertTrue(carModel.isPresent());
        assertTrue(category.isPresent());
        assertTrue(manufacturer.isPresent());

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> carRepository.save(car));

    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_car_models.sql", "/sql/insert_categories.sql", "/sql/insert_manufacturers.sql", "/sql/insert_cars.sql"})
    void findAll_shouldReturnCarList() {
        List<Car> carList = carRepository.findAll();
        assertEquals(9, carList.size());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_car_models.sql", "/sql/insert_categories.sql", "/sql/insert_manufacturers.sql", "/sql/insert_cars.sql"})
    void deleteById_shouldNotDeleteCarFromDatabase_whenInputContainsNotExistingCarId() {
        long carId = 100;
        carRepository.deleteById(carId);
        assertEquals(9, carRepository.count());
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_car_models.sql", "/sql/insert_categories.sql", "/sql/insert_manufacturers.sql", "/sql/insert_cars.sql"})
    void findById_shouldReturnCar_whenInputContainsExistingCarId() {

        long carId = 3;

        long manufacturerId = 2;
        long carModelId = 3;
        long categoryId = 5;

        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(manufacturerId);
        Optional<CarModel> carModel = carModelRepository.findById(carModelId);
        Optional<Category> category = categoryRepository.findById(categoryId);

        Car expectedCar = new Car();

        expectedCar.setCarId(carId);
        expectedCar.setManufacturer(manufacturer.get());
        expectedCar.setManufactureYear(2018);
        expectedCar.setCarModel(carModel.get());
        expectedCar.setCategory(category.get());

        Optional<Car> actualCar = carRepository.findById(carId);

        assertTrue(carModel.isPresent());
        assertTrue(category.isPresent());
        assertTrue(manufacturer.isPresent());
        assertTrue(actualCar.isPresent());

        assertEquals(expectedCar, actualCar.get());

    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_car_models.sql", "/sql/insert_categories.sql", "/sql/insert_manufacturers.sql", "/sql/insert_cars.sql"})
    void findCarByCarId_shouldReturnNull_whenInputContainsNotExistingCarId() {
        long carId = 100;
        Optional<Car> actualCar = carRepository.findById(carId);
        assertTrue(actualCar.isEmpty());
    }
    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_car_models.sql", "/sql/insert_categories.sql", "/sql/insert_manufacturers.sql", "/sql/insert_cars.sql"})
    void existsByCarModel_CarModelId_shouldReturnTrue_whenInputContainsExistingCarModelId() {
        long carModelId = 4;
        boolean isCarModelExists = carRepository.existsByCarModel_CarModelId(carModelId);
        assertTrue(isCarModelExists);
    }

    @Test
    @Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_car_models.sql", "/sql/insert_categories.sql", "/sql/insert_manufacturers.sql", "/sql/insert_cars.sql"})
    void existsByCarModel_CarModelId_shouldReturnFalse_whenInputContainsNotExistingCarModelId() {
        long carModelId = 100;
        boolean isCarModelExists = carRepository.existsByCarModel_CarModelId(carModelId);
        assertFalse(isCarModelExists);
    }

}
