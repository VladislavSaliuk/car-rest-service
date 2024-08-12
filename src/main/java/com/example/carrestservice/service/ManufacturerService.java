package com.example.carrestservice.service;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.CarModelNameException;
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
    private ManufacturerRepository manufacturerRepository;

    public ManufacturerService(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    public Manufacturer createManufacturer(Manufacturer manufacturer) {

        if (manufacturer == null) {
            throw new IllegalArgumentException("Manufacturer cannot be null!");
        }

        if (manufacturerRepository.existsByManufacturerName(manufacturer.getManufacturerName())) {
            throw new ManufacturerNameException("Manufacturer name " + manufacturer.getManufacturerName() + " already exists!");
        }

        return manufacturerRepository.save(manufacturer);

    }

    public void updateManufacturer(Manufacturer manufacturer) {

        if (manufacturer == null) {
            throw new IllegalArgumentException("Manufacturer cannot be null!");
        }

        Manufacturer updatedManufacturer = manufacturerRepository.findById(manufacturer.getManufacturerId())
                .orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer with Id " + manufacturer.getManufacturerId() + " not found."));

        Optional.of(manufacturer.getManufacturerName())
                .filter(manufacturerName -> !manufacturerRepository.existsByManufacturerName(manufacturerName))
                .ifPresentOrElse(
                        updatedManufacturerName -> updatedManufacturer.setManufacturerName(updatedManufacturerName),
                        () -> {
                            throw new ManufacturerNameException("Manufacturer name " + manufacturer.getManufacturerName() + " already exists!");
                        }
                );

    }

    public void removeById(long manufacturerId) {

        if(!manufacturerRepository.existsByManufacturerId(manufacturerId)) {
            throw new ManufacturerNotFoundException("Manufacturer with Id " + manufacturerId + " not found.");
        } else {
            manufacturerRepository.deleteById(manufacturerId);
        }

    }

    public Page<Manufacturer> getAll(Pageable pageable) {

        if (pageable.getOffset() < 0) {
            throw new IllegalArgumentException("Offset must be a non-negative integer.");
        }

        if (pageable.getPageSize() <= 0) {
            throw new IllegalArgumentException("Page size must be a positive integer.");
        }

        return manufacturerRepository.findAll(pageable);
    }

    public Manufacturer getById(long manufacturerId) {
        return manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ManufacturerNotFoundException("Manufacturer with Id " + manufacturerId + " not found."));
    }

}
