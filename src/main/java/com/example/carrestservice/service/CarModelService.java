package com.example.carrestservice.service;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.exception.CarModelNameException;
import com.example.carrestservice.exception.CarModelNotFoundException;
import com.example.carrestservice.repository.CarModelRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarModelService {

    private CarModelRepository carModelRepository;

    public CarModelService(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    public CarModel createCarModel(CarModel carModel) {

        if (carModel == null) {
            throw new IllegalArgumentException("Car model cannot be null!");
        }

        if(carModelRepository.existsByCarModelName(carModel.getCarModelName())) {
            throw new CarModelNotFoundException("Car model name " + carModel.getCarModelName() + " already exists!");
        }

        return carModelRepository.save(carModel);

    }

    @Transactional
    public void updateCarModel(CarModel carModel) {

        if (carModel == null) {
            throw new IllegalArgumentException("Car model cannot be null!");
        }

        CarModel updatedCarModel = carModelRepository.findById(carModel.getCarModelId())
                .orElseThrow(() -> new CarModelNotFoundException("Car model with Id " + carModel.getCarModelId() + " not found."));

        Optional.of(carModel.getCarModelName())
                .filter(carModelName -> !carModelRepository.existsByCarModelName(carModelName))
                .ifPresentOrElse(
                        updatedCarModelName -> updatedCarModel.setCarModelName(updatedCarModelName),
                        () -> {
                            throw new CarModelNameException("Car model name " + carModel.getCarModelName() + " already exists!");
                        }
                );
    }

    public void removeById(long carModelId) {

        if(!carModelRepository.existsByCarModelId(carModelId)) {
            throw new CarModelNotFoundException("Car model with Id " + carModelId + " not found.");
        } else {
            carModelRepository.deleteById(carModelId);
        }

    }
    public Page<CarModel> getAll(Pageable pageable) {

        if (pageable.getOffset() < 0) {
            throw new IllegalArgumentException("Offset must be a non-negative integer.");
        }
        if (pageable.getPageSize() <= 0) {
            throw new IllegalArgumentException("Page size must be a positive integer.");
        }

        return carModelRepository.findAll(pageable);
    }

    public CarModel getById(long carModelId) {
        return carModelRepository.findById(carModelId)
                .orElseThrow(() -> new CarModelNotFoundException("Car model with Id " + carModelId + " not found."));
    }


}
