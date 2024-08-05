package com.example.carrestservice.service;

import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.ManufacturerNameException;
import com.example.carrestservice.exception.ManufacturerNotFoundException;
import com.example.carrestservice.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManufacturerService {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public Manufacturer createManufacturer(Manufacturer manufacturer) {

        if(manufacturer == null) {
            throw new IllegalArgumentException("Manufacturer cannot be null!");
        }

        Optional<Manufacturer> existingManufacturer = manufacturerRepository.findByManufacturerName(manufacturer.getManufacturerName());

        if(existingManufacturer.isPresent()) {
            throw new ManufacturerNameException("Manufacturer name " + manufacturer.getManufacturerName() + " already exists!");
        }

        return manufacturerRepository.save(manufacturer);

    }

    public Manufacturer updateManufacturer(Manufacturer manufacturer) {

        if(manufacturer == null) {
            throw new IllegalArgumentException("Manufacturer cannot be null!");
        }

        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findById(manufacturer.getManufacturerId());

        return optionalManufacturer.map(updatedManufacturer -> {

            updatedManufacturer.setManufacturerName(manufacturer.getManufacturerName());

            boolean isManufacturerNameTaken = manufacturerRepository.existsByManufacturerName(manufacturer.getManufacturerName());

            if(isManufacturerNameTaken) {
                throw new ManufacturerNameException("Manufacturer name " + updatedManufacturer.getManufacturerName() + " already exists!");
            }

            return manufacturerRepository.save(updatedManufacturer);

        }).orElseThrow(() ->
            new ManufacturerNotFoundException("Manufacturer with Id " + manufacturer.getManufacturerId() + " not found.")
        );

    }

    public Manufacturer removeById(long manufacturerId) {

        Manufacturer manufacturer = manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer with Id " + manufacturerId + " not found."));

        manufacturerRepository.deleteById(manufacturerId);

        return manufacturer;
    }

    public List<Manufacturer> getAll() {
        return manufacturerRepository.findAll();
    }
    public List<Manufacturer> getAll(String sortDirection, String sortField) {

        if(!isValidSortField(sortField)) {
            throw new IllegalArgumentException("Invalid sort field : " + sortField);
        }

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Sort sort = Sort.by(direction, sortField);
        return manufacturerRepository.findAll(sort);
    }

    private boolean isValidSortField(String sortField) {

        Field[] fields = Manufacturer.class.getDeclaredFields();

        List<String> fieldList = Arrays.stream(fields)
                .map(Field::getName)
                .collect(Collectors.toList());

        return fieldList.contains(sortField);
    }
    public Page<Manufacturer> getPage(int offset, int pageSize) {

        if (offset < 0) {
            throw new IllegalArgumentException("Offset must be a non-negative integer.");
        }
        if (pageSize <= 0) {
            throw new IllegalArgumentException("Page size must be a positive integer.");
        }

        Pageable pageable = PageRequest.of(offset, pageSize);
        return manufacturerRepository.findAll(pageable);
    }

    public Manufacturer getById(long manufacturerId) {
        return manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer with Id " + manufacturerId + " not found."));
    }

}
