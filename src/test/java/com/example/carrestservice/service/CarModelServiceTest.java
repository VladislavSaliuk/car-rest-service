package com.example.carrestservice.service;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.exception.CarModelNameException;
import com.example.carrestservice.exception.CarModelNotFoundException;
import com.example.carrestservice.repository.CarModelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CarModelServiceTest {


    @Autowired
    CarModelService carModelService;

    @MockBean
    CarModelRepository carModelRepository;

    @Test
    void createCarModel_shouldReturnCarModel_whenInputContainsCarModel() {

        String carModelName = "Test car model name";

        CarModel expectedCarModel = new CarModel();
        expectedCarModel.setCarModelName(carModelName);

        when(carModelRepository.findByCarModelName(carModelName))
                .thenReturn(Optional.empty());

        when(carModelRepository.save(expectedCarModel))
                .thenReturn(expectedCarModel);

        CarModel actualCarModel = carModelService.createCarModel(expectedCarModel);

        assertNotNull(actualCarModel);
        assertEquals(expectedCarModel, actualCarModel);

        verify(carModelRepository).findByCarModelName(carModelName);
        verify(carModelRepository).save(expectedCarModel);

    }

    @Test
    void createCarModel_shouldThrowException_whenInputContainsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carModelService.createCarModel(null));

        assertEquals("Car model cannot be null!", exception.getMessage());

        verify(carModelRepository, never()).findByCarModelName(null);
        verify(carModelRepository, never()).save(null);

    }

    @Test
    void createCarModel_shouldThrowException_whenInputContainsCarModelWithExistingCarModelName() {

        String carModelName = "A4";

        CarModel carModel = new CarModel();
        carModel.setCarModelName(carModelName);

        when(carModelRepository.findByCarModelName(carModelName))
                .thenReturn(Optional.ofNullable(carModel));

        CarModelNotFoundException exception = assertThrows(CarModelNotFoundException.class, () -> carModelService.createCarModel(carModel));

        assertEquals("Car model name " + carModel.getCarModelName() + " already exists!", exception.getMessage());

        verify(carModelRepository).findByCarModelName(carModelName);
        verify(carModelRepository, never()).save(carModel);

    }
    @Test
    void updateCarModel_shouldReturnCarModel_whenInputContainsCarModel() {

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        CarModel updatedCarModel = new CarModel();
        updatedCarModel.setCarModelId(carModelId);
        updatedCarModel.setCarModelName("Test car model name 1");

        when(carModelRepository.findById(carModelId))
                .thenReturn(Optional.ofNullable(carModel));

        when(carModelRepository.existsByCarModelName(updatedCarModel.getCarModelName()))
                .thenReturn(false);

        when(carModelRepository.save(updatedCarModel))
                .thenReturn(updatedCarModel);

        CarModel actualCarModel = carModelService.updateCarModel(updatedCarModel);

        assertNotNull(actualCarModel);
        assertEquals(updatedCarModel, actualCarModel);

        verify(carModelRepository).findById(carModelId);
        verify(carModelRepository).existsByCarModelName(updatedCarModel.getCarModelName());
        verify(carModelRepository).save(updatedCarModel);

    }

    @Test
    void updateCarModel_shouldThrowException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carModelService.updateCarModel(null));
        assertEquals("Car model cannot be null!", exception.getMessage());

        verify(carModelRepository, never()).findById(0L);
        verify(carModelRepository, never()).save(null);
    }

    @Test
    void updateCarModel_shouldThrowException_whenInputContainsCarModelWithExistingCarModelName() {

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        CarModel updatedCarModel = new CarModel();
        updatedCarModel.setCarModelId(carModelId);
        updatedCarModel.setCarModelName("Mustang");

        when(carModelRepository.findById(carModelId))
                .thenReturn(Optional.ofNullable(carModel));

        when(carModelRepository.existsByCarModelName(updatedCarModel.getCarModelName()))
                .thenReturn(true);

        CarModelNameException exception = assertThrows(CarModelNameException.class, () -> carModelService.updateCarModel(updatedCarModel));

        assertEquals("Car model name " + updatedCarModel.getCarModelName() + " already exists!", exception.getMessage());

        verify(carModelRepository).findById(carModelId);
        verify(carModelRepository).existsByCarModelName(updatedCarModel.getCarModelName());
        verify(carModelRepository, never()).save(updatedCarModel);

    }

    @Test
    void updateCarModel_shouldThrowException_whenInputContainsCarModelWithNotExistingCarModelId() {

        long carModelId = 100;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelRepository.findById(carModelId))
                .thenReturn(Optional.empty());

        CarModelNotFoundException exception = assertThrows(CarModelNotFoundException.class, () -> carModelService.updateCarModel(carModel));

        assertEquals("Car model with Id " + carModel.getCarModelId() + " not found.", exception.getMessage());

        verify(carModelRepository).findById(carModelId);
        verify(carModelRepository, never()).existsByCarModelName(null);
        verify(carModelRepository, never()).save(null);

    }

    @Test
    void removeById_returnCarModel_whenInputContainsExistingCarModelId() {

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel expectedCarModel = new CarModel();
        expectedCarModel.setCarModelId(carModelId);
        expectedCarModel.setCarModelName(carModelName);

        when(carModelRepository.findById(carModelId))
                .thenReturn(Optional.ofNullable(expectedCarModel));

        doNothing().when(carModelRepository).deleteById(carModelId);

        CarModel actualCarModel = carModelService.removeById(carModelId);

        assertNotNull(actualCarModel);
        assertEquals(expectedCarModel, actualCarModel);

        verify(carModelRepository).findById(carModelId);
        verify(carModelRepository).deleteById(carModelId);

    }

    @Test
    void removeById_shouldThrowException_whenInputContainsNotExistingCarModelId() {

        long carModelId = 100;

        when(carModelRepository.findById(carModelId))
                .thenReturn(Optional.empty());

        CarModelNotFoundException exception = assertThrows(CarModelNotFoundException.class, () -> carModelService.removeById(carModelId));

        assertEquals("Car model with Id " + carModelId + " not found.", exception.getMessage());

        verify(carModelRepository).findById(carModelId);
        verify(carModelRepository, never()).deleteById(carModelId);

    }

    @Test
    void getAll_shouldReturnCarModelList_whenInputContainsNothing() {

        when(carModelRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<CarModel> carModelList = carModelService.getAll();

        assertNotNull(carModelList);
        assertTrue(carModelList.isEmpty());

        verify(carModelRepository).findAll();

    }

    @Test
    void getAll_shouldReturnCarModelList_whenInputContainsCorrectData() {

        String sortDirection = "ASC";
        String sortField = "carModelName";

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Sort sort = Sort.by(direction, sortField);

        when(carModelRepository.findAll(sort))
                .thenReturn(Collections.emptyList());

        List<CarModel> carModelList = carModelService.getAll();

        assertNotNull(carModelList);
        assertTrue(carModelList.isEmpty());

        verify(carModelRepository).findAll();

    }

    @Test
    void getAll_shouldThrowException_whenInputContainsNotExistingSortField() {

        String sortDirection = "ASC";
        String sortField = "Test sort field";


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carModelService.getAll(sortDirection, sortField));

        assertEquals("Invalid sort field : " + sortField, exception.getMessage());

        verify(carModelRepository, never()).findAll();

    }

    @Test
    void getPage_shouldReturnPage_whenInputContainsCorrectData() {

        int offset = 10;
        int pageSize = 10;

        Pageable pageable = PageRequest.of(offset, pageSize);

        Page<CarModel> expectedCarModelPage = new PageImpl<>(Collections.emptyList(),pageable,0);

        when(carModelRepository.findAll(pageable))
                .thenReturn(expectedCarModelPage);

        Page<CarModel> actualCarModelPage = carModelService.getPage(offset, pageSize);

        assertNotNull(actualCarModelPage);
        assertEquals(expectedCarModelPage, actualCarModelPage);

        verify(carModelRepository).findAll(pageable);

    }

    @Test
    void getPage_shouldThrowException_whenInputContainsNegativeOffset() {

        int offset = -10;
        int pageSize = 10;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carModelService.getPage(offset, pageSize));

        assertEquals("Offset must be a non-negative integer.", exception.getMessage());

        verify(carModelRepository, never()).findAll();

    }

    @Test
    void getPage_shouldThrowException_whenInputContainsNegativePageSize() {

        int offset = 10;
        int pageSize = -10;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carModelService.getPage(offset, pageSize));

        assertEquals("Page size must be a positive integer.", exception.getMessage());

        verify(carModelRepository, never()).findAll();

    }

    @Test
    void getById_shouldReturnCarModel_whenInputContainsExistingCarModelId() {

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel expectedCarModel = new CarModel();
        expectedCarModel.setCarModelId(carModelId);
        expectedCarModel.setCarModelName(carModelName);

        when(carModelRepository.findById(carModelId))
                .thenReturn(Optional.ofNullable(expectedCarModel));

        CarModel actualCarModel = carModelService.getById(carModelId);

        assertNotNull(actualCarModel);
        assertEquals(expectedCarModel, actualCarModel);

        verify(carModelRepository).findById(carModelId);

    }

    @Test
    void getById_shouldThrowException_whenInputContainsNotExistingCarModelId() {

        long carModelId = 100;

        when(carModelRepository.findById(carModelId))
                .thenReturn(Optional.empty());

        CarModelNotFoundException exception = assertThrows(CarModelNotFoundException.class, () -> carModelService.getById(carModelId));

        assertEquals("Car model with Id " + carModelId + " not found.", exception.getMessage());

        verify(carModelRepository).findById(carModelId);

    }


}
