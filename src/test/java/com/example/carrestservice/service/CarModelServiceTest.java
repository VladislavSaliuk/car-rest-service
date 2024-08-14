package com.example.carrestservice.service;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.exception.CarModelNameException;
import com.example.carrestservice.exception.CarModelNotFoundException;
import com.example.carrestservice.repository.CarModelRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CarModelServiceTest {

    @Autowired
    CarModelService carModelService;

    @MockBean
    CarModelRepository carModelRepository;
    static CarModel carModel;
    @BeforeAll
    static void init() {
        carModel = new CarModel();
        carModel.setCarModelId(1L);
        carModel.setCarModelName("Test");
    }

    static Stream<Pageable> pageableProvider() {
        return Stream.of(
                PageRequest.of(0, 5),
                PageRequest.of(1, 10),
                PageRequest.of(2, 20),
                PageRequest.of(3, 25),
                PageRequest.of(4, 30),
                PageRequest.of(5, 40),
                PageRequest.of(6, 45),
                PageRequest.of(7, 50),
                PageRequest.of(8, 55),
                PageRequest.of(9, 60)
        );
    }

    @Test
    void createCarModel_shouldReturnCarModel_whenInputContainsCarModel() {

        when(carModelRepository.existsByCarModelName(carModel.getCarModelName()))
                .thenReturn(false);

        when(carModelRepository.save(carModel))
                .thenReturn(carModel);

        CarModel actualCarModel = carModelService.createCarModel(carModel);

        assertNotNull(actualCarModel);
        assertEquals(carModel, actualCarModel);

        verify(carModelRepository).existsByCarModelName(carModel.getCarModelName());
        verify(carModelRepository).save(carModel);

    }

    @Test
    void createCarModel_shouldThrowException_whenInputContainsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> carModelService.createCarModel(null));

        assertEquals("Car model cannot be null!", exception.getMessage());

        verify(carModelRepository, never()).existsByCarModelName(null);
        verify(carModelRepository, never()).save(null);

    }

    @Test
    void createCarModel_shouldThrowException_whenInputContainsCarModelWithExistingCarModelName() {

        when(carModelRepository.existsByCarModelName(carModel.getCarModelName()))
                .thenReturn(true);

        CarModelNotFoundException exception = assertThrows(CarModelNotFoundException.class, () -> carModelService.createCarModel(carModel));

        assertEquals("Car model name " + carModel.getCarModelName() + " already exists!", exception.getMessage());

        verify(carModelRepository).existsByCarModelName(carModel.getCarModelName());
        verify(carModelRepository, never()).save(carModel);

    }
    @Test
    void updateCarModel_shouldUpdateCarModel_whenInputContainsCarModel() {

        when(carModelRepository.findById(carModel.getCarModelId()))
                .thenReturn(Optional.of(carModel));

        when(carModelRepository.existsByCarModelName(carModel.getCarModelName()))
                .thenReturn(false);

        carModelService.updateCarModel(carModel);

        verify(carModelRepository).findById(carModel.getCarModelId());
        verify(carModelRepository).existsByCarModelName(carModel.getCarModelName());
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

        when(carModelRepository.findById(carModel.getCarModelId()))
                .thenReturn(Optional.ofNullable(carModel));

        when(carModelRepository.existsByCarModelName(carModel.getCarModelName()))
                .thenReturn(true);

        CarModelNameException exception = assertThrows(CarModelNameException.class, () -> carModelService.updateCarModel(carModel));

        assertEquals("Car model name " + carModel.getCarModelName() + " already exists!", exception.getMessage());

        verify(carModelRepository).findById(carModel.getCarModelId());
        verify(carModelRepository).existsByCarModelName(carModel.getCarModelName());

    }



    @Test
    void updateCarModel_shouldThrowException_whenInputContainsCarModelWithNotExistingCarModelId() {

        when(carModelRepository.findById(carModel.getCarModelId()))
                .thenReturn(Optional.empty());

        CarModelNotFoundException exception = assertThrows(CarModelNotFoundException.class, () -> carModelService.updateCarModel(carModel));

        assertEquals("Car model with Id " + carModel.getCarModelId() + " not found.", exception.getMessage());

        verify(carModelRepository).findById(carModel.getCarModelId());
        verify(carModelRepository, never()).existsByCarModelName(null);
        verify(carModelRepository, never()).save(null);

    }

    @Test
    void removeById_shouldRemoveCarModel_whenInputContainsExistingCarModelId() {

        long carModelId = 1L;

        when(carModelRepository.existsByCarModelId(carModelId))
                .thenReturn(true);

        doNothing().when(carModelRepository).deleteById(carModelId);

        carModelService.removeById(carModelId);

        verify(carModelRepository).existsByCarModelId(carModelId);
        verify(carModelRepository).deleteById(carModelId);

    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void removeById_shouldThrowException_whenInputContainsNotExistingCarModelId(long carModelId) {

        when(carModelRepository.existsByCarModelId(carModelId))
                .thenReturn(false);

        CarModelNotFoundException exception = assertThrows(CarModelNotFoundException.class,() -> carModelService.removeById(carModelId));

        assertEquals("Car model with Id " + carModelId + " not found.", exception.getMessage());

        verify(carModelRepository).existsByCarModelId(carModelId);
        verify(carModelRepository,never()).deleteById(carModelId);

    }

    @ParameterizedTest
    @MethodSource("pageableProvider")
    void getAll_shouldReturnCarModelPage_whenInputContainsCorrectPageable(Pageable pageable) {

        Page page = new PageImpl(List.of(carModel), pageable, 2);

        when(carModelRepository.findAll(pageable))
                .thenReturn(page);

        Page actualPage = carModelService.getAll(pageable);

        assertEquals(page, actualPage);

        verify(carModelRepository).findAll(pageable);

    }
    @Test
    void getById_shouldReturnCarModel_whenInputContainsExistingCarModelId() {

        long carModelId = 1L;

        when(carModelRepository.findById(carModelId))
                .thenReturn(Optional.ofNullable(carModel));

        CarModel actualCarModel = carModelService.getById(carModelId);

        assertNotNull(actualCarModel);
        assertEquals(carModel, actualCarModel);

        verify(carModelRepository).findById(carModelId);

    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void getById_shouldThrowException_whenInputContainsNotExistingCarModelId(long carModelId) {

        when(carModelRepository.findById(carModelId))
                .thenReturn(Optional.empty());

        CarModelNotFoundException exception = assertThrows(CarModelNotFoundException.class, () -> carModelService.getById(carModelId));

        assertEquals("Car model with Id " + carModelId + " not found.", exception.getMessage());

        verify(carModelRepository).findById(carModelId);

    }


}
