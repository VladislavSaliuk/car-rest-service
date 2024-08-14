package com.example.carrestservice.rest;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.service.CarModelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarModelRestController {

    private CarModelService carModelService;

    public CarModelRestController(CarModelService carModelService) {
        this.carModelService = carModelService;
    }

    @PostMapping("/car-models")
    @ResponseStatus(HttpStatus.CREATED)
    public CarModel createCarModel(@RequestBody CarModel carModel) {
        return carModelService.createCarModel(carModel);
    }

    @PutMapping("/car-models")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCarModel(@RequestBody CarModel carModel) {
        carModelService.updateCarModel(carModel);
    }

    @DeleteMapping("/car-models/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCarModelById(@PathVariable("id") Long carModelId) {
        carModelService.removeById(carModelId);
    }

    @GetMapping("/car-models")
    @ResponseStatus(HttpStatus.OK)
    public Page<CarModel> getCarModels(
            @RequestParam(required = false, defaultValue = "carModelId") String sortField,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int pageSize){


        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(offset, pageSize,sort);

        return carModelService.getAll(pageable);
    }

    @GetMapping("/car-models/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarModel getCarModelById(@PathVariable Long id) {
        return carModelService.getById(id);
    }


}
