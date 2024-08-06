package com.example.carrestservice.rest;

import com.example.carrestservice.entity.CarModel;
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

    @PostMapping("/car-models")
    public ResponseEntity<CarModel> createCarModel(@RequestBody CarModel carModel) {
        carModelService.createCarModel(carModel);
        return new ResponseEntity<>(carModel, HttpStatus.CREATED);
    }

    @PutMapping("/car-models")
    public ResponseEntity<CarModel> updateCarModel(@RequestBody CarModel carModel) {
        carModelService.updateCarModel(carModel);
        return new ResponseEntity<>(carModel, HttpStatus.OK);
    }

    @DeleteMapping("/car-models/{id}")
    public ResponseEntity<CarModel> removeCarModelById(@PathVariable("id") Long carModelId) {
        CarModel carModel = carModelService.removeById(carModelId);
        return new ResponseEntity<>(carModel, HttpStatus.OK);
    }

    @GetMapping("/car-models")
    public ResponseEntity<?> getCarModels() {
        List<CarModel> carModelList = carModelService.getAll();
        return new ResponseEntity<>(carModelList, HttpStatus.OK);
    }

    @GetMapping("/car-models/sort")
    public ResponseEntity<List<CarModel>> getSortedCarModels(@RequestParam(required = false, defaultValue = "carModelId") String sortField,
                                                    @RequestParam(required = false, defaultValue = "DESC") String sortDirection){
        List<CarModel> carModelList = carModelService.getAll(sortDirection, sortField);
        return new ResponseEntity<>(carModelList, HttpStatus.OK);
    }

    @GetMapping("/car-models/pagination")
    public ResponseEntity<List<CarModel>> getCarModelPage(
            @RequestParam int offset,
            @RequestParam int pageSize) {

        Page<CarModel> carModelPage = carModelService.getPage(offset, pageSize);
        return new ResponseEntity<>(carModelPage.getContent(), HttpStatus.OK);
    }


    @GetMapping("/car-models/{id}")
    public ResponseEntity<CarModel> getCarModelById(@PathVariable("id") Long carModelId) {
        CarModel carModel = carModelService.getById(carModelId);
        return new ResponseEntity<>(carModel, HttpStatus.OK);
    }


}
