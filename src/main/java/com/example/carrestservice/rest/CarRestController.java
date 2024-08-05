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
    @PostMapping("/cars/create")
    public ResponseEntity<?> createCar(@RequestBody Car car) {
        try {
            carService.createCar(car);
            return new ResponseEntity<>(car, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/cars/update")
    public ResponseEntity<?> updateCar(@RequestBody Car car) {
        try {
            carService.updateCar(car);
            return new ResponseEntity<>(car, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cars/remove/{id}")
    public ResponseEntity<?> removeCarById(@PathVariable("id") Long carId) {
        try {
            Car car = carService.removeById(carId);
            return new ResponseEntity<>(car, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cars")
    public ResponseEntity<?> getCars() {
        List<Car> carList = carService.getAll();
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    @GetMapping("/cars/sort")
    public ResponseEntity<?> getSortedCars(@RequestParam(required = false, defaultValue = "carId") String sortField,
                                           @RequestParam(required = false, defaultValue = "DESC") String sortDirection){
        try {
            List<Car> carList = carService.getAll(sortDirection, sortField);
            return new ResponseEntity<>(carList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cars/pagination")
    public ResponseEntity<?> getCarPage(
            @RequestParam int offset,
            @RequestParam int pageSize) {

        try {
            Page<Car> carPage = carService.getPage(offset, pageSize);
            return new ResponseEntity<>(carPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/cars/{id}")
    public ResponseEntity<?> getCarById(@PathVariable("id") Long carId) {
        try {
            Car car = carService.getById(carId);
            return new ResponseEntity<>(car, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

}
