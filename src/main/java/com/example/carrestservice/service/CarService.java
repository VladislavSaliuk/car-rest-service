package com.example.carrestservice.service;

import com.example.carrestservice.entity.Car;
import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Category;
import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.CarException;
import com.example.carrestservice.exception.CarModelNameException;
import com.example.carrestservice.exception.CarModelNotFoundException;
import com.example.carrestservice.exception.CarNotFoundException;
import com.example.carrestservice.repository.CarRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarService {

    private CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car createCar(Car car) {

        if(car == null) {
            throw new IllegalArgumentException("Car cannot be null!");
        }

        if(carRepository.existsByCarModel_CarModelId(car.getCarModel().getCarModelId())) {
            throw new CarException("Car with car model Id " + car.getCarModel().getCarModelId() + " already exist!");
        }

        return carRepository.save(car);
    }

    @Transactional
    public void updateCar(Car car) {

        if(car == null) {
            throw new IllegalArgumentException("Car cannot be null!");
        }

        Car updatedCar = carRepository.findById(car.getCarId())
                .orElseThrow(() -> new CarNotFoundException("Car with Id " + car.getCarId() + " not found."));

        Optional.of(car.getCarModel())
                .filter(carModel -> carRepository.existsByCarModel_CarModelId(carModel.getCarModelId()))
                .ifPresentOrElse(
                        carModel -> updatedCar.setCarModel(carModel),
                        () -> {
                            throw new CarException("Car model with Id " + car.getCarModel().getCarModelId() + " already exists!");
                        }
                );

        updatedCar.setManufacturer(car.getManufacturer());
        updatedCar.setManufactureYear(car.getManufactureYear());
        updatedCar.setCategory(car.getCategory());
    }

    public void removeById(long carId) {

       if(!carRepository.existsByCarId(carId)) {
           throw new CarNotFoundException("Car with Id " + carId + " not found!");
       } else {
            carRepository.deleteById(carId);
       }

    }

    public Page<Car> getAll(Pageable pageable) {

        if (pageable.getOffset() < 0) {
            throw new IllegalArgumentException("Offset must be a non-negative integer.");
        }
        if (pageable.getPageSize() <= 0) {
            throw new IllegalArgumentException("Page size must be a positive integer.");
        }

        return carRepository.findAll(pageable);
    }


    public Car getById(long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car with Id " + carId + " not found."));
    }

}
