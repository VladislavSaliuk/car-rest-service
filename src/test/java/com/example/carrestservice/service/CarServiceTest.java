package com.example.carrestservice.service;

import com.example.carrestservice.entity.Car;
import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Category;
import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.CarException;
import com.example.carrestservice.exception.CarNotFoundException;
import com.example.carrestservice.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CarServiceTest {


    @Autowired
    CarService carService;

    @MockBean
    CarRepository carRepository;


    @Test
    void createCar_shouldReturnCar_whenInputContainsCar() {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);
        manufacturer.setCars(Collections.emptySet());

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();

        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();

        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setCars(Collections.emptySet());

        Car car = new Car();

        car.setManufacturer(manufacturer);
        car.setManufactureYear(2024);
        car.setCarModel(carModel);
        car.setCategory(category);

        when(carRepository.existsByCarModel_CarModelId(carModelId))
                .thenReturn(false);

        when(carRepository.save(car))
                .thenReturn(car);

        Car actualCar = carService.createCar(car);

        assertNotNull(actualCar);
        assertEquals(car, actualCar);

        verify(carRepository).existsByCarModel_CarModelId(carModelId);
        verify(carRepository).save(car);

    }

    @Test
    void createCar_shouldThrowException_whenInputContainsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carService.createCar(null));

        assertEquals("Car cannot be null!", exception.getMessage());

        verify(carRepository, never()).existsByCarModel_CarModelId(0L);
        verify(carRepository, never()).save(null);

    }

    @Test
    void createCar_shouldThrowException_whenInputContainsCarWithExistingCarModel() {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);
        manufacturer.setCars(Collections.emptySet());

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();

        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();

        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setCars(Collections.emptySet());

        Car car = new Car();

        car.setManufacturer(manufacturer);
        car.setManufactureYear(2024);
        car.setCarModel(carModel);
        car.setCategory(category);

        when(carRepository.existsByCarModel_CarModelId(carModelId))
                .thenReturn(true);

        CarException exception = assertThrows(CarException.class,() -> carService.createCar(car));

        assertEquals("Car with car model Id " + car.getCarModel().getCarModelId() + " already exist!", exception.getLocalizedMessage());

        verify(carRepository).existsByCarModel_CarModelId(carModelId);
        verify(carRepository, never()).save(car);

    }

    @Test
    void updateCar_shouldReturnCar_whenInputContainsCar() {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);
        manufacturer.setCars(Collections.emptySet());

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();

        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();

        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setCars(Collections.emptySet());

        long carId = 1;

        Car car = new Car();

        car.setCarId(carId);
        car.setManufacturer(manufacturer);
        car.setManufactureYear(2024);
        car.setCarModel(carModel);
        car.setCategory(category);

        Car updatedCar = new Car();

        updatedCar.setCarId(carId);
        updatedCar.setManufacturer(manufacturer);
        updatedCar.setManufactureYear(2020);
        updatedCar.setCarModel(carModel);
        updatedCar.setCategory(category);

        when(carRepository.findById(car.getCarId()))
                .thenReturn(Optional.ofNullable(car));

        when(carRepository.existsByCarModel_CarModelId(updatedCar.getCarModel().getCarModelId()))
                .thenReturn(false);

        when(carRepository.save(updatedCar))
                .thenReturn(updatedCar);

        Car actualCar = carService.updateCar(updatedCar);

        assertNotNull(actualCar);
        assertEquals(updatedCar, actualCar);

        verify(carRepository).findById(car.getCarId());
        verify(carRepository).existsByCarModel_CarModelId(updatedCar.getCarModel().getCarModelId());
        verify(carRepository).save(updatedCar);

    }

    @Test
    void updateCar_shouldThrowException_whenInputContainsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carService.updateCar(null));
        assertEquals("Car cannot be null!", exception.getMessage());

        verify(carRepository, never()).findById(0L);
        verify(carRepository, never()).existsByCarModel_CarModelId(0L);
        verify(carRepository, never()).save(null);

    }

    @Test
    void updateCar_shouldThrowException_whenInputContainsCarWithExistingCarModel() {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);
        manufacturer.setCars(Collections.emptySet());

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();

        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();

        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setCars(Collections.emptySet());

        long carId = 1;

        Car car = new Car();

        car.setCarId(carId);
        car.setManufacturer(manufacturer);
        car.setManufactureYear(2024);
        car.setCarModel(carModel);
        car.setCategory(category);

        Car updatedCar = new Car();

        updatedCar.setCarId(carId);
        updatedCar.setManufacturer(manufacturer);
        updatedCar.setManufactureYear(2020);
        updatedCar.setCarModel(carModel);
        updatedCar.setCategory(category);

        when(carRepository.findById(car.getCarId()))
                .thenReturn(Optional.ofNullable(car));

        when(carRepository.existsByCarModel_CarModelId(updatedCar.getCarModel().getCarModelId()))
                .thenReturn(true);

        CarException exception = assertThrows(CarException.class, () -> carService.updateCar(updatedCar));

        assertEquals("Car with car model Id " + updatedCar.getCarModel().getCarModelId() + " already exist!", exception.getMessage());

        verify(carRepository).findById(car.getCarId());
        verify(carRepository).existsByCarModel_CarModelId(updatedCar.getCarModel().getCarModelId());
        verify(carRepository, never()).save(updatedCar);

    }

    @Test
    void updateCar_shouldThrowException_whenInputContainsNotExistingCar() {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);
        manufacturer.setCars(Collections.emptySet());

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();

        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();

        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setCars(Collections.emptySet());

        long carId = 1;

        Car car = new Car();

        car.setCarId(carId);
        car.setManufacturer(manufacturer);
        car.setManufactureYear(2024);
        car.setCarModel(carModel);
        car.setCategory(category);

        when(carRepository.findById(car.getCarId()))
                .thenReturn(Optional.empty());


        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> carService.updateCar(car));

        assertEquals("Car with Id " + car.getCarModel().getCarModelId() + " not found!", exception.getMessage());

        verify(carRepository).findById(car.getCarId());
        verify(carRepository, never()).existsByCarModel_CarModelId(0L);
        verify(carRepository, never()).save(null);

    }

    @Test
    void removeById_shouldReturnCar_whenInputContainsExistingCarId() {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);
        manufacturer.setCars(Collections.emptySet());

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();

        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();

        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setCars(Collections.emptySet());

        long carId = 1;

        Car expectedCar = new Car();

        expectedCar.setCarId(carId);
        expectedCar.setManufacturer(manufacturer);
        expectedCar.setManufactureYear(2024);
        expectedCar.setCarModel(carModel);
        expectedCar.setCategory(category);

        when(carRepository.findById(carId))
                .thenReturn(Optional.ofNullable(expectedCar));

        doNothing().when(carRepository).deleteById(carId);

        Car actualCar = carService.removeById(carId);

        assertNotNull(actualCar);
        assertEquals(expectedCar, actualCar);

        verify(carRepository).findById(carId);
        verify(carRepository).deleteById(carId);

    }

    @Test
    void removeById_shouldThrowException_whenInputContainsNotExistingCarId() {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);
        manufacturer.setCars(Collections.emptySet());

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();

        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();

        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setCars(Collections.emptySet());

        long carId = 100;

        Car expectedCar = new Car();

        expectedCar.setCarId(carId);
        expectedCar.setManufacturer(manufacturer);
        expectedCar.setManufactureYear(2024);
        expectedCar.setCarModel(carModel);
        expectedCar.setCategory(category);

        when(carRepository.findById(carId))
                .thenReturn(Optional.empty());

        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> carService.removeById(carId));

        assertEquals("Car with Id " + carId + " not found.", exception.getMessage());

        verify(carRepository).findById(carId);
        verify(carRepository, never()).deleteById(carId);

    }

    @Test
    void getAll_shouldReturnCarList() {

        when(carRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Car> carList = carService.getAll();

        assertTrue(carList.isEmpty());

        verify(carRepository).findAll();

    }


    @Test
    void getById_shouldReturnCar_whenInputContainsExistingCarId() {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);
        manufacturer.setCars(Collections.emptySet());

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();

        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        long categoryId = 1;
        String categoryName = "Test category name";

        Category category = new Category();

        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        category.setCars(Collections.emptySet());

        long carId = 1;

        Car expectedCar = new Car();

        expectedCar.setCarId(carId);
        expectedCar.setManufacturer(manufacturer);
        expectedCar.setManufactureYear(2024);
        expectedCar.setCarModel(carModel);
        expectedCar.setCategory(category);

        when(carRepository.findById(carId))
                .thenReturn(Optional.ofNullable(expectedCar));

        Car actualCar = carService.getById(carId);

        assertNotNull(actualCar);
        assertEquals(expectedCar, actualCar);

        verify(carRepository).findById(carId);

    }

    @Test
    void getById_shouldReturnCat_whenInputContainsNotExistingCarId() {

        long carId = 100;

        when(carRepository.findById(carId))
                .thenReturn(Optional.empty());

        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> carService.getById(carId));

        assertEquals("Car with Id " + carId + " not found.", exception.getMessage());

        verify(carRepository).findById(carId);

    }

}
