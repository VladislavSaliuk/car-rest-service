package com.example.carrestservice.rest;

import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.ApiError;
import com.example.carrestservice.service.ManufacturerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ManufacturerRestController {

    @Autowired
    private ManufacturerService manufacturerService;

    private static final Logger logger = LoggerFactory.getLogger(ManufacturerRestController.class);
    @PostMapping("/manufacturers/create")
    public ResponseEntity<?> createManufacturer(@RequestBody Manufacturer manufacturer) {
        try {
            manufacturerService.createManufacturer(manufacturer);
            return new ResponseEntity<>(manufacturer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/manufacturers/update")
    public ResponseEntity<?> updateManufacturer(@RequestBody Manufacturer manufacturer) {
        try {
            manufacturerService.updateManufacturer(manufacturer);
            return new ResponseEntity<>(manufacturer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("manufacturers/remove/{id}")
    public ResponseEntity<?> removeManufacturerById(@PathVariable("id") Long manufacturerId) {
        try {
            Manufacturer manufacturer = manufacturerService.removeById(manufacturerId);
            return new ResponseEntity<>(manufacturer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/manufacturers")
    public ResponseEntity<?> getManufacturers(){
        List<Manufacturer> manufacturerList = manufacturerService.getAll();
        return new ResponseEntity<>(manufacturerList,HttpStatus.OK);
    }

    @GetMapping("/manufacturers/sort")
    public ResponseEntity<?> getSortedManufacturers(@RequestParam(required = false, defaultValue = "manufacturerId") String sortField,
                                              @RequestParam(required = false, defaultValue = "DESC") String sortDirection){
        try {
            List<Manufacturer> manufacturerList = manufacturerService.getAll(sortDirection, sortField);
            return new ResponseEntity<>(manufacturerList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/manufacturers/pagination")
    public ResponseEntity<?> getManufacturerPage(
            @RequestParam int offset,
            @RequestParam int pageSize) {

        try {
            Page<Manufacturer> manufacturerPage = manufacturerService.getPage(offset, pageSize);
            return new ResponseEntity<>(manufacturerPage.getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/manufacturers/{id}")
    public ResponseEntity<?> getManufacturerById(@PathVariable("id") Long manufacturerId) {
        try {
            Manufacturer manufacturer = manufacturerService.getById(manufacturerId);
            return new ResponseEntity<>(manufacturer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }




}
