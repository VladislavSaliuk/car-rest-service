package com.example.carrestservice.rest;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Category;
import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.ApiError;
import com.example.carrestservice.service.CarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarModelRestController {

    @Autowired
    private CarModelService carModelService;

    @PostMapping("/car-models/create")
    public ResponseEntity<?> createCarModel(@RequestBody CarModel carModel) {
        try {
            carModelService.createCarModel(carModel);
            return new ResponseEntity<>(carModel, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/car-models/update")
    public ResponseEntity<?> updateCarModel(@RequestBody CarModel carModel) {
        try {
            carModelService.updateCarModel(carModel);
            return new ResponseEntity<>(carModel, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/car-models/remove/{id}")
    public ResponseEntity<?> removeCarModelById(@PathVariable("id") Long carModelId) {
        try {
            CarModel carModel = carModelService.removeById(carModelId);
            return new ResponseEntity<>(carModel, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/car-models")
    public ResponseEntity<?> getCarModels() {
        List<CarModel> carModelList = carModelService.getAll();
        return new ResponseEntity<>(carModelList, HttpStatus.OK);
    }

    @GetMapping("/car-models/sort")
    public ResponseEntity<?> getSortedCarModels(@RequestParam(required = false, defaultValue = "carModelId") String sortField,
                                                    @RequestParam(required = false, defaultValue = "DESC") String sortDirection){
        try {
            List<CarModel> carModelList = carModelService.getAll(sortDirection, sortField);
            return new ResponseEntity<>(carModelList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/car-models/pagination")
    public ResponseEntity<?> getCarModelPage(
            @RequestParam int offset,
            @RequestParam int pageSize) {

        try {
            Page<CarModel> carModelPage = carModelService.getPage(offset, pageSize);
            return new ResponseEntity<>(carModelPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/car-models/{id}")
    public ResponseEntity<?> getCarModelById(@PathVariable("id") Long carModelId) {
        try {
            CarModel carModel = carModelService.getById(carModelId);
            return new ResponseEntity<>(carModel, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }


}
