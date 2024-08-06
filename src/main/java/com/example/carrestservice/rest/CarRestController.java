package com.example.carrestservice.rest;

import com.example.carrestservice.entity.Car;
import com.example.carrestservice.entity.Category;
import com.example.carrestservice.exception.ApiError;
import com.example.carrestservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarRestController {

    @Autowired
    private CarService carService;
    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        carService.createCar(car);
        return new ResponseEntity<>(car, HttpStatus.CREATED);
    }

    @PutMapping("/cars")
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        carService.updateCar(car);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Car> removeCarById(@PathVariable("id") Long carId) {
        Car car = carService.removeById(carId);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getCars() {
        List<Car> carList = carService.getAll();
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    @GetMapping("/cars/sort")
    public ResponseEntity<List<Car>> getSortedCars(@RequestParam(required = false, defaultValue = "carId") String sortField,
                                           @RequestParam(required = false, defaultValue = "DESC") String sortDirection){
        List<Car> carList = carService.getAll(sortDirection, sortField);
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    @GetMapping("/cars/pagination")
    public ResponseEntity<List<Car>> getCarPage(
            @RequestParam int offset,
            @RequestParam int pageSize) {

        Page<Car> carPage = carService.getPage(offset, pageSize);
        return new ResponseEntity<>(carPage.getContent(), HttpStatus.OK);
    }
    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable("id") Long carId) {
        Car car = carService.getById(carId);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

}
