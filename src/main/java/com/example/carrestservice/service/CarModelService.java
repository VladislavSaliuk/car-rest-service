package com.example.carrestservice.service;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.exception.CarModelNameException;
import com.example.carrestservice.exception.CarModelNotFoundException;
import com.example.carrestservice.repository.CarModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarModelService {

    @Autowired
    private CarModelRepository carModelRepository;


    public CarModel createCarModel(CarModel carModel) {

        if (carModel == null) {
            throw new IllegalArgumentException("Car model cannot be null!");
        }

        Optional<CarModel> existingCarModel = carModelRepository.findByCarModelName(carModel.getCarModelName());

        if(existingCarModel.isPresent()) {
            throw new CarModelNotFoundException("Car model name " + carModel.getCarModelName() + " already exists!");
        }

        return carModelRepository.save(carModel);

    }

    public CarModel updateCarModel(CarModel carModel) {

        if (carModel == null) {
            throw new IllegalArgumentException("Car model cannot be null!");
        }

        Optional<CarModel> optionalCarModel = carModelRepository.findById(carModel.getCarModelId());

        return optionalCarModel.map(updatedCarModel -> {

            updatedCarModel.setCarModelName(carModel.getCarModelName());

            boolean isCarModelNameTaken = carModelRepository.existsByCarModelName(updatedCarModel.getCarModelName());

            if (isCarModelNameTaken) {
                throw new CarModelNameException("Car model name " + updatedCarModel.getCarModelName() + " already exists!");
            }

            return carModelRepository.save(updatedCarModel);

        }).orElseThrow(() ->
                new CarModelNotFoundException("Car model with Id " + carModel.getCarModelId() + " not found.")
        );
    }


    public CarModel removeById(long carModelId) {

        CarModel carModel = carModelRepository.findById(carModelId)
                .orElseThrow(() -> new CarModelNotFoundException("Car model with Id " + carModelId + " not found."));

        carModelRepository.deleteById(carModelId);

        return carModel;
    }
    public List<CarModel> getAll() {
        return carModelRepository.findAll();
    }

    public CarModel getById(long carModelId) {
        return carModelRepository.findById(carModelId)
                .orElseThrow(() -> new CarModelNotFoundException("Car model with Id " + carModelId + " not found."));
    }



}
