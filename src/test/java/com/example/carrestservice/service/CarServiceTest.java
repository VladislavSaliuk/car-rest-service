package com.example.carrestservice.service;

import com.example.carrestservice.entity.Car;
import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Category;
import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.CarException;
import com.example.carrestservice.exception.CarNotFoundException;
import com.example.carrestservice.repository.CarRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @InjectMocks
    CarService carService;

    @Mock
    CarRepository carRepository;

    static Car car;

    @BeforeAll
    static void init() {

        CarModel carModel = new CarModel();
        carModel.setCarModelId(1L);
        carModel.setCarModelName("Test");

        Category category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("Test");

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(1L);
        manufacturer.setManufacturerName("Test");

        car = Car.builder()
                .carId(1L)
                .manufacturer(manufacturer)
                .manufactureYear(2024)
                .carModel(carModel)
                .category(category)
                .build();
    }
    static Stream<Pageable> pageableProvider() {
        return Stream.of(
                PageRequest.of(0, 5),
                PageRequest.of(1, 10),
                PageRequest.of(2, 20),
                PageRequest.of(3, 25),
                PageRequest.of(4, 30),
                PageRequest.of(5, 40),
                PageRequest.of(6, 45),
                PageRequest.of(7, 50),
                PageRequest.of(8, 55),
                PageRequest.of(9, 60)
        );

    }
    @Test
    void createCar_shouldReturnCar_whenInputIsValid() {

        when(carRepository.existsByCarModel_CarModelId(car.getCarModel().getCarModelId()))
                .thenReturn(false);

        when(carRepository.save(car)).thenReturn(car);

        Car actualCar = carService.createCar(car);

        assertNotNull(actualCar);
        assertEquals(car, actualCar);

        verify(carRepository).existsByCarModel_CarModelId(car.getCarModel().getCarModelId());
        verify(carRepository).save(car);
    }

    @Test
    void createCar_shouldThrowException_whenInputIsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carService.createCar(null));

        assertEquals("Car cannot be null!", exception.getMessage());

        verify(carRepository, never()).existsByCarModel_CarModelId(anyLong());
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void createCar_shouldThrowException_whenCarModelAlreadyExists() {

        when(carRepository.existsByCarModel_CarModelId(car.getCarModel().getCarModelId()))
                .thenReturn(true);

        CarException exception = assertThrows(CarException.class, () -> carService.createCar(car));

        assertEquals("Car with car model Id " + car.getCarModel().getCarModelId() + " already exist!", exception.getMessage());

        verify(carRepository).existsByCarModel_CarModelId(car.getCarModel().getCarModelId());
        verify(carRepository, never()).save(car);
    }

    @Test
    void updateCar_shouldUpdateCar_whenInputContainsCar() {

        when(carRepository.findById(car.getCarId()))
                .thenReturn(Optional.of(car));

        when(carRepository.existsByCarModel_CarModelId(car.getCarModel().getCarModelId()))
                .thenReturn(true);

        carService.updateCar(car);


        verify(carRepository).findById(car.getCarId());
        verify(carRepository).existsByCarModel_CarModelId(car.getCarModel().getCarModelId());

    }

    @Test
    void updateCar_shouldThrowException_whenInputIsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carService.updateCar(null));
        assertEquals("Car cannot be null!", exception.getMessage());

        verify(carRepository, never()).findById(anyLong());
        verify(carRepository, never()).save(any(Car.class));

    }

    @Test
    void updateCar_shouldThrowException_whenCarNotFound() {

        when(carRepository.findById(car.getCarId()))
                .thenReturn(Optional.empty());

        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> carService.updateCar(car));

        assertEquals("Car with Id " + car.getCarId() + " not found.", exception.getMessage());

        verify(carRepository).findById(car.getCarId());
        verify(carRepository, never()).save(any(Car.class));

    }

    @Test
    void updateCar_shouldThrowException_whenCarModelDoesNotExist() {

        when(carRepository.findById(car.getCarId())).thenReturn(Optional.of(car));
        when(carRepository.existsByCarModel_CarModelId(car.getCarModel().getCarModelId())).thenReturn(false);

        CarException exception = assertThrows(CarException.class, () -> carService.updateCar(car));

        assertEquals("Car model with Id " + car.getCarModel().getCarModelId() + " already exists!", exception.getMessage());

        verify(carRepository).findById(car.getCarId());
        verify(carRepository).existsByCarModel_CarModelId(car.getCarModel().getCarModelId());

    }

    @Test
    void removeById_shouldRemoveCar_whenCarExists() {

        long carId = 1L;

        when(carRepository.existsByCarId(carId)).thenReturn(true);
        doNothing().when(carRepository).deleteById(carId);

        carService.removeById(carId);

        verify(carRepository).existsByCarId(carId);
        verify(carRepository).deleteById(carId);
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void removeById_shouldThrowException_whenCarDoesNotExist(long carId) {

        when(carRepository.existsByCarId(carId)).thenReturn(false);

        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> carService.removeById(carId));

        assertEquals("Car with Id " + carId + " not found!", exception.getMessage());

        verify(carRepository).existsByCarId(carId);
        verify(carRepository, never()).deleteById(carId);

    }

    @ParameterizedTest
    @MethodSource("pageableProvider")
    void getAll_shouldReturnPageOfCars_whenInputIsValid(Pageable pageable) {

        Page<Car> page = new PageImpl<>(List.of(car), pageable, 2);

        when(carRepository.findAll(pageable)).thenReturn(page);

        Page<Car> actualPage = carService.getAll(pageable);

        assertEquals(page, actualPage);

        verify(carRepository).findAll(pageable);
    }

    @Test
    void getById_shouldReturnCar_whenCarExists() {
        long carId = 1L;

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        Car actualCar = carService.getById(carId);

        assertNotNull(actualCar);
        assertEquals(car, actualCar);

        verify(carRepository).findById(carId);
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void getById_shouldThrowException_whenCarDoesNotExist(long carId) {

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> carService.getById(carId));

        assertEquals("Car with Id " + carId + " not found.", exception.getMessage());

        verify(carRepository).findById(carId);
    }

}
