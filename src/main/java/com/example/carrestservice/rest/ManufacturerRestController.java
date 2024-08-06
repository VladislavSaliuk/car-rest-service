package com.example.carrestservice.rest;

import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.ApiError;
import com.example.carrestservice.service.ManufacturerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ManufacturerRestController {

    @Autowired
    private ManufacturerService manufacturerService;

    private static final Logger logger = LoggerFactory.getLogger(ManufacturerRestController.class);
    @PostMapping("/manufacturers")
    public ResponseEntity<Manufacturer> createManufacturer(@RequestBody Manufacturer manufacturer) {
        manufacturerService.createManufacturer(manufacturer);
        return new ResponseEntity<>(manufacturer, HttpStatus.CREATED);
    }

    @PutMapping("/manufacturers")
    public ResponseEntity<?> updateManufacturer(@RequestBody Manufacturer manufacturer) {
        manufacturerService.updateManufacturer(manufacturer);
        return new ResponseEntity<>(manufacturer, HttpStatus.OK);
    }
    @DeleteMapping("manufacturers/{id}")
    public ResponseEntity<Manufacturer> removeManufacturerById(@PathVariable("id") Long manufacturerId) {
        Manufacturer manufacturer = manufacturerService.removeById(manufacturerId);
        return new ResponseEntity<>(manufacturer, HttpStatus.OK);
    }

    @GetMapping("/manufacturers")
    public ResponseEntity<List<Manufacturer>> getManufacturers(){
        List<Manufacturer> manufacturerList = manufacturerService.getAll();
        return new ResponseEntity<>(manufacturerList,HttpStatus.OK);
    }

    @GetMapping("/manufacturers/sort")
    public ResponseEntity<List<Manufacturer>> getSortedManufacturers(@RequestParam(required = false, defaultValue = "manufacturerId") String sortField,
                                              @RequestParam(required = false, defaultValue = "DESC") String sortDirection){

        List<Manufacturer> manufacturerList = manufacturerService.getAll(sortDirection, sortField);
        return new ResponseEntity<>(manufacturerList, HttpStatus.OK);
    }

    @GetMapping("/manufacturers/pagination")
    public ResponseEntity<List<Manufacturer>> getManufacturerPage(
            @RequestParam int offset,
            @RequestParam int pageSize) {

        Page<Manufacturer> manufacturerPage = manufacturerService.getPage(offset, pageSize);
        return new ResponseEntity<>(manufacturerPage.getContent(), HttpStatus.OK);
    }

    @GetMapping("/manufacturers/{id}")
    public ResponseEntity<Manufacturer> getManufacturerById(@PathVariable("id") Long manufacturerId) {

        Manufacturer manufacturer = manufacturerService.getById(manufacturerId);
        return new ResponseEntity<>(manufacturer, HttpStatus.OK);
    }




}
