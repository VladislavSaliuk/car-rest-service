package com.example.carrestservice.rest;

import com.example.carrestservice.entity.Car;
import com.example.carrestservice.entity.Category;
import com.example.carrestservice.exception.ApiError;
import com.example.carrestservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarRestController {
    private CarService carService;
    public CarRestController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/cars")
    @ResponseStatus(HttpStatus.CREATED)
    public Car createCar(@RequestBody Car car) {
        return carService.createCar(car);
    }

    @PutMapping("/cars")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCar(@RequestBody Car car) {
        carService.updateCar(car);
    }
    @DeleteMapping("/cars/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCarById(@PathVariable("id") Long carId) {
        carService.removeById(carId);
    }

    @GetMapping("/cars")
    @ResponseStatus(HttpStatus.OK)
    public Page<Car> getCars(
            @RequestParam(required = false, defaultValue = "carId") String sortField,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(offset, pageSize,sort);

        return carService.getAll(pageable);
    }

    @GetMapping("/cars/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Car getCarById(@PathVariable Long id) {
        return carService.getById(id);
    }

}
