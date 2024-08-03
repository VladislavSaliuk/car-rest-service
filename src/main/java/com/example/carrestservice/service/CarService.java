package com.example.carrestservice.service;

import com.example.carrestservice.entity.Car;
import com.example.carrestservice.exception.CarException;
import com.example.carrestservice.exception.CarNotFoundException;
import com.example.carrestservice.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;


    public Car createCar(Car car) {

        if(car == null) {
            throw new IllegalArgumentException("Car cannot be null!");
        }

        if(carRepository.existsByCarModel_CarModelId(car.getCarModel().getCarModelId())) {
            throw new CarException("Car with car model Id " + car.getCarModel().getCarModelId() + " already exist!");
        }

        return carRepository.save(car);
    }

    public Car updateCar(Car car) {

        if(car == null) {
            throw new IllegalArgumentException("Car cannot be null!");
        }

        Optional<Car> optionalCar = carRepository.findById(car.getCarId());

        return optionalCar.map(updatedCar -> {

            updatedCar.setManufacturer(car.getManufacturer());
            updatedCar.setManufactureYear(car.getManufactureYear());
            updatedCar.setCarModel(car.getCarModel());
            updatedCar.setCategory(car.getCategory());

            boolean isCarModelTaken = carRepository.existsByCarModel_CarModelId(car.getCarModel().getCarModelId());

            if (isCarModelTaken) {
                throw new CarException("Car with car model Id " + updatedCar.getCarModel().getCarModelId() + " already exist!");
            }

            return carRepository.save(updatedCar);

        }).orElseThrow(() ->
                new CarNotFoundException("Car with Id " + car.getCarId() + " not found!")
        );

    }

    public Car removeById(long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car with Id " + carId + " not found."));

        carRepository.deleteById(carId);

        return car;
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public Car getById(long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car with Id " + carId + " not found."));
    }

}
