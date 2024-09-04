package com.example.carrestservice.service;

import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.ManufacturerNameException;
import com.example.carrestservice.exception.ManufacturerNotFoundException;
import com.example.carrestservice.repository.ManufacturerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManufacturerServiceTest {

    @InjectMocks
    ManufacturerService manufacturerService;

    @Mock
    ManufacturerRepository manufacturerRepository;

    Manufacturer manufacturer;

    @BeforeEach
    void setUp() {
        manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(1L);
        manufacturer.setManufacturerName("Test");
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
    void createManufacturer_shouldReturnManufacturer_whenInputContainsManufacturer() {

        when(manufacturerRepository.existsByManufacturerName(manufacturer.getManufacturerName()))
                .thenReturn(false);

        when(manufacturerRepository.save(manufacturer))
                .thenReturn(manufacturer);

        Manufacturer actualManufacturer = manufacturerService.createManufacturer(manufacturer);

        assertNotNull(actualManufacturer);
        assertEquals(manufacturer, actualManufacturer);

        verify(manufacturerRepository).existsByManufacturerName(manufacturer.getManufacturerName());
        verify(manufacturerRepository).save(manufacturer);
    }

    @Test
    void createManufacturer_shouldThrowException_whenInputContainsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> manufacturerService.createManufacturer(null));

        assertEquals("Manufacturer cannot be null!", exception.getMessage());

        verify(manufacturerRepository, never()).existsByManufacturerName(null);
        verify(manufacturerRepository, never()).save(null);
    }

    @Test
    void createManufacturer_shouldThrowException_whenInputContainsManufacturerWithExistingName() {

        when(manufacturerRepository.existsByManufacturerName(manufacturer.getManufacturerName()))
                .thenReturn(true);

        ManufacturerNameException exception = assertThrows(ManufacturerNameException.class, () -> manufacturerService.createManufacturer(manufacturer));

        assertEquals("Manufacturer name " + manufacturer.getManufacturerName() + " already exists!", exception.getMessage());

        verify(manufacturerRepository).existsByManufacturerName(manufacturer.getManufacturerName());
        verify(manufacturerRepository, never()).save(manufacturer);
    }

    @Test
    void updateManufacturer_shouldUpdateManufacturer_whenInputContainsManufacturer() {

        when(manufacturerRepository.findById(manufacturer.getManufacturerId()))
                .thenReturn(Optional.of(manufacturer));

        when(manufacturerRepository.existsByManufacturerName(manufacturer.getManufacturerName()))
                .thenReturn(false);

        manufacturerService.updateManufacturer(manufacturer);

        verify(manufacturerRepository).findById(manufacturer.getManufacturerId());
        verify(manufacturerRepository).existsByManufacturerName(manufacturer.getManufacturerName());
    }

    @Test
    void updateManufacturer_shouldThrowException_whenInputContainsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> manufacturerService.updateManufacturer(null));
        assertEquals("Manufacturer cannot be null!", exception.getMessage());

        verify(manufacturerRepository, never()).findById(0L);
        verify(manufacturerRepository, never()).save(null);
    }

    @Test
    void updateManufacturer_shouldThrowException_whenInputContainsManufacturerWithExistingName() {

        when(manufacturerRepository.findById(manufacturer.getManufacturerId()))
                .thenReturn(Optional.ofNullable(manufacturer));

        when(manufacturerRepository.existsByManufacturerName(manufacturer.getManufacturerName()))
                .thenReturn(true);

        ManufacturerNameException exception = assertThrows(ManufacturerNameException.class, () -> manufacturerService.updateManufacturer(manufacturer));

        assertEquals("Manufacturer name " + manufacturer.getManufacturerName() + " already exists!", exception.getMessage());

        verify(manufacturerRepository).findById(manufacturer.getManufacturerId());
        verify(manufacturerRepository).existsByManufacturerName(manufacturer.getManufacturerName());
    }

    @Test
    void updateManufacturer_shouldThrowException_whenInputContainsManufacturerWithNotExistingId() {

        when(manufacturerRepository.findById(manufacturer.getManufacturerId()))
                .thenReturn(Optional.empty());

        ManufacturerNotFoundException exception = assertThrows(ManufacturerNotFoundException.class, () -> manufacturerService.updateManufacturer(manufacturer));

        assertEquals("Manufacturer with Id " + manufacturer.getManufacturerId() + " not found.", exception.getMessage());

        verify(manufacturerRepository).findById(manufacturer.getManufacturerId());
        verify(manufacturerRepository, never()).existsByManufacturerName(null);
        verify(manufacturerRepository, never()).save(null);
    }

    @Test
    void removeById_shouldRemoveManufacturer_whenInputContainsExistingManufacturerId() {

        long manufacturerId = 1L;

        when(manufacturerRepository.existsByManufacturerId(manufacturerId))
                .thenReturn(true);

        doNothing().when(manufacturerRepository).deleteById(manufacturerId);

        manufacturerService.removeById(manufacturerId);

        verify(manufacturerRepository).existsByManufacturerId(manufacturerId);
        verify(manufacturerRepository).deleteById(manufacturerId);
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void removeById_shouldThrowException_whenInputContainsNotExistingManufacturerId(long manufacturerId) {

        when(manufacturerRepository.existsByManufacturerId(manufacturerId))
                .thenReturn(false);

        ManufacturerNotFoundException exception = assertThrows(ManufacturerNotFoundException.class, () -> manufacturerService.removeById(manufacturerId));

        assertEquals("Manufacturer with Id " + manufacturerId + " not found.", exception.getMessage());

        verify(manufacturerRepository).existsByManufacturerId(manufacturerId);
        verify(manufacturerRepository, never()).deleteById(manufacturerId);
    }

    @ParameterizedTest
    @MethodSource("pageableProvider")
    void getAll_shouldReturnManufacturerPage_whenInputContainsCorrectPageable(Pageable pageable) {

        Page<Manufacturer> page = new PageImpl<>(List.of(manufacturer), pageable, 2);

        when(manufacturerRepository.findAll(pageable))
                .thenReturn(page);

        Page<Manufacturer> actualPage = manufacturerService.getAll(pageable);

        assertEquals(page, actualPage);

        verify(manufacturerRepository).findAll(pageable);
    }

    @Test
    void getById_shouldReturnManufacturer_whenInputContainsExistingManufacturerId() {

        long manufacturerId = 1L;

        when(manufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.ofNullable(manufacturer));

        Manufacturer actualManufacturer = manufacturerService.getById(manufacturerId);

        assertNotNull(actualManufacturer);
        assertEquals(manufacturer, actualManufacturer);

        verify(manufacturerRepository).findById(manufacturerId);
    }

    @ParameterizedTest
    @ValueSource(longs = {11L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L})
    void getById_shouldThrowException_whenInputContainsNotExistingManufacturerId(long manufacturerId) {

        when(manufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.empty());

        ManufacturerNotFoundException exception = assertThrows(ManufacturerNotFoundException.class, () -> manufacturerService.getById(manufacturerId));

        assertEquals("Manufacturer with Id " + manufacturerId + " not found.", exception.getMessage());

        verify(manufacturerRepository).findById(manufacturerId);
    }
}
