package com.example.carrestservice.rest;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.ApiError;
import com.example.carrestservice.service.ManufacturerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ManufacturerRestController {
    private ManufacturerService manufacturerService;

    public ManufacturerRestController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }
    @PostMapping("/manufacturers")
    @ResponseStatus(HttpStatus.CREATED)
    public Manufacturer createManufacturer(@RequestBody Manufacturer manufacturer) {
        return manufacturerService.createManufacturer(manufacturer);
    }

    @PutMapping("/manufacturers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateManufacturer(@RequestBody Manufacturer manufacturer) {
        manufacturerService.updateManufacturer(manufacturer);
    }

    @DeleteMapping("manufacturers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeManufacturerById(@PathVariable("id") Long manufacturerId) {
        manufacturerService.removeById(manufacturerId);
    }

    @GetMapping("/manufacturers")
    @ResponseStatus(HttpStatus.OK)
    public Page<Manufacturer> getManufacturers(
            @RequestParam(required = false, defaultValue = "manufacturerId") String sortField,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int pageSize){

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Sort sort = Sort.by(direction, sortField);

        Pageable pageable = PageRequest.of(offset, pageSize,sort);

        return manufacturerService.getAll(pageable);
    }


    @GetMapping("/manufacturers/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Manufacturer getManufacturerById(@PathVariable Long id) {
        return manufacturerService.getById(id);
    }


}
