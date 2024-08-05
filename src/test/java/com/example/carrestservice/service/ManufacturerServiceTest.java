package com.example.carrestservice.service;

import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.ManufacturerNameException;
import com.example.carrestservice.exception.ManufacturerNotFoundException;
import com.example.carrestservice.repository.ManufacturerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ManufacturerServiceTest {

    @Autowired
    ManufacturerService manufacturerService;

    @MockBean
    ManufacturerRepository manufacturerRepository;

    @Test
    void createManufacturer_shouldReturnManufacturer_whenInputContainsManufacturer() {

        String manufacturerName = "Test manufacturer name";

        Manufacturer expectedManufacturer = new Manufacturer();

        expectedManufacturer.setManufacturerName(manufacturerName);
        expectedManufacturer.setCars(Collections.emptySet());

        when(manufacturerRepository.findByManufacturerName(manufacturerName))
                .thenReturn(Optional.empty());

        when(manufacturerRepository.save(expectedManufacturer))
                .thenReturn(expectedManufacturer);

        Manufacturer actualManufacturer = manufacturerService.createManufacturer(expectedManufacturer);

        assertNotNull(actualManufacturer);
        assertEquals(expectedManufacturer, actualManufacturer);

        verify(manufacturerRepository).findByManufacturerName(manufacturerName);
        verify(manufacturerRepository).save(expectedManufacturer);

    }

    @Test
    void createManufacturer_shouldThrowException_whenInputContainsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> manufacturerService.createManufacturer(null));
        assertEquals("Manufacturer cannot be null!", exception.getMessage());

        verify(manufacturerRepository, never()).findByManufacturerName(null);
        verify(manufacturerRepository, never()).save(null);
    }

    @Test
    void createManufacturer_shouldThrowException_whenInputContainsManufacturerWithExistingManufacturerName() {

        String manufacturerName = "Nissan";

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerName(manufacturerName);
        manufacturer.setCars(Collections.emptySet());

        when(manufacturerRepository.findByManufacturerName(manufacturerName))
                .thenReturn(Optional.ofNullable(manufacturer));

        ManufacturerNameException exception = assertThrows(ManufacturerNameException.class, () -> manufacturerService.createManufacturer(manufacturer));

        assertEquals("Manufacturer name " + manufacturer.getManufacturerName() + " already exists!", exception.getMessage());

        verify(manufacturerRepository).findByManufacturerName(manufacturerName);
        verify(manufacturerRepository, never()).save(manufacturer);

    }

    @Test
    void updateManufacturer_shouldReturnManufacturer_whenInputContainsManufacturer() {

        long manufacturerId = 1;
        String manufacturerName  = "Test manufacturerName";

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);
        manufacturer.setCars(Collections.emptySet());

        Manufacturer updatedManufacturer = new Manufacturer();

        updatedManufacturer.setManufacturerId(manufacturerId);
        updatedManufacturer.setManufacturerName("Test manufacturer name 1");
        updatedManufacturer.setCars(Collections.emptySet());

        when(manufacturerRepository.findById(manufacturer.getManufacturerId()))
                .thenReturn(Optional.ofNullable(manufacturer));

        when(manufacturerRepository.existsByManufacturerName(updatedManufacturer.getManufacturerName()))
                .thenReturn(false);

        when(manufacturerRepository.save(updatedManufacturer))
                .thenReturn(updatedManufacturer);

        Manufacturer actualManufacturer = manufacturerService.updateManufacturer(updatedManufacturer);

        assertNotNull(actualManufacturer);
        assertEquals(updatedManufacturer, actualManufacturer);

        verify(manufacturerRepository).findById(manufacturer.getManufacturerId());
        verify(manufacturerRepository).existsByManufacturerName(updatedManufacturer.getManufacturerName());
        verify(manufacturerRepository).save(updatedManufacturer);

    }

    @Test
    void updateManufacturer_shouldThrowException_whenInputContainsNull() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> manufacturerService.updateManufacturer(null));
        assertEquals("Manufacturer cannot be null!", exception.getMessage());

        verify(manufacturerRepository, never()).findByManufacturerName(null);
        verify(manufacturerRepository, never()).save(null);

    }

    @Test
    void updateManufacturer_shouldThrowException_whenInputContainsManufacturerWithExistingManufacturerName() {

        long manufacturerId = 1;
        String manufacturerName  = "Test manufacturerName";

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);
        manufacturer.setCars(Collections.emptySet());

        Manufacturer updatedManufacturer = new Manufacturer();

        updatedManufacturer.setManufacturerId(manufacturerId);
        updatedManufacturer.setManufacturerName("Nissan");
        updatedManufacturer.setCars(Collections.emptySet());

        when(manufacturerRepository.findById(manufacturer.getManufacturerId()))
                .thenReturn(Optional.ofNullable(manufacturer));

        when(manufacturerRepository.existsByManufacturerName(updatedManufacturer.getManufacturerName()))
                .thenReturn(true);

        ManufacturerNameException exception = assertThrows(ManufacturerNameException.class, () -> manufacturerService.updateManufacturer(updatedManufacturer));

        assertEquals("Manufacturer name " + updatedManufacturer.getManufacturerName() + " already exists!", exception.getMessage() );

        verify(manufacturerRepository).findById(manufacturer.getManufacturerId());
        verify(manufacturerRepository).existsByManufacturerName(updatedManufacturer.getManufacturerName());
        verify(manufacturerRepository, never()).save(updatedManufacturer);

    }

    @Test
    void updateManufacturer_shouldThrowException_whenInputContainsManufacturerWithNotExistingManufacturerId() {

        long manufacturerId = 100;

        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName("Nissan");
        manufacturer.setCars(Collections.emptySet());

        when(manufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.empty());

        ManufacturerNotFoundException exception = assertThrows(ManufacturerNotFoundException.class, () -> manufacturerService.updateManufacturer(manufacturer));

        assertEquals("Manufacturer with Id " + manufacturer.getManufacturerId() + " not found.", exception.getMessage() );

        verify(manufacturerRepository).findById(manufacturer.getManufacturerId());
        verify(manufacturerRepository, never()).existsByManufacturerName(null);
        verify(manufacturerRepository, never()).save(null);

    }


    @Test
    void removeById_shouldReturnManufacturer_whenInputContainsExistingManufacturerId() {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer expectedManufacturer = new Manufacturer();

        expectedManufacturer.setManufacturerId(manufacturerId);
        expectedManufacturer.setManufacturerName(manufacturerName);
        expectedManufacturer.setCars(Collections.emptySet());

        when(manufacturerRepository.findById(expectedManufacturer.getManufacturerId()))
                .thenReturn(Optional.ofNullable(expectedManufacturer));

        doNothing().when(manufacturerRepository).deleteById(manufacturerId);

        Manufacturer actualManufacturer = manufacturerService.removeById(expectedManufacturer.getManufacturerId());

        assertNotNull(actualManufacturer);
        assertEquals(expectedManufacturer, actualManufacturer);

        verify(manufacturerRepository).findById(manufacturerId);
        verify(manufacturerRepository).deleteById(manufacturerId);

    }

    @Test
    void removeById_shouldThrowException_whenInputContainsNotExistingManufacturerId() {

        long manufacturerId = 100;

        when(manufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.empty());

        ManufacturerNotFoundException exception = assertThrows(ManufacturerNotFoundException.class, () -> manufacturerService.removeById(manufacturerId));

        assertEquals("Manufacturer with Id " + manufacturerId + " not found.", exception.getMessage());

        verify(manufacturerRepository).findById(manufacturerId);
        verify(manufacturerRepository, never()).deleteById(manufacturerId);

    }

    @Test
    void getAll_shouldReturnManufacturerList() {

        when(manufacturerRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Manufacturer> manufacturerList = manufacturerService.getAll();

        assertTrue(manufacturerList.isEmpty());

        verify(manufacturerRepository).findAll();

    }

    @Test
    void getAll_shouldReturnManufacturerList_whenInputContainsCorrectData() {

        String sortDirection = "ASC";
        String sortField = "manufacturerName";

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);

        Sort sort = Sort.by(direction, sortField);

        when(manufacturerRepository.findAll(sort))
                .thenReturn(Collections.emptyList());

        List<Manufacturer> manufacturerList = manufacturerService.getAll();

        assertNotNull(manufacturerList);
        assertTrue(manufacturerList.isEmpty());

        verify(manufacturerRepository).findAll();

    }

    @Test
    void getAll_shouldThrowException_whenInputContainsNotExistingSortField() {

        String sortDirection = "ASC";
        String sortField = "Test sort field";


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> manufacturerService.getAll(sortDirection, sortField));

        assertEquals("Invalid sort field : " + sortField, exception.getMessage());

        verify(manufacturerRepository, never()).findAll();

    }

    @Test
    void getPage_shouldReturnPage_whenInputContainsCorrectData() {

        int offset = 10;
        int pageSize = 10;

        Pageable pageable = PageRequest.of(offset, pageSize);

        Page<Manufacturer> expectedManufacturerPage = new PageImpl<>(Collections.emptyList(),pageable,0);

        when(manufacturerRepository.findAll(pageable))
                .thenReturn(expectedManufacturerPage);

        Page<Manufacturer> actualManufacturerPage = manufacturerService.getPage(offset, pageSize);

        assertNotNull(actualManufacturerPage);
        assertEquals(expectedManufacturerPage, actualManufacturerPage);

        verify(manufacturerRepository).findAll(pageable);

    }

    @Test
    void getPage_shouldThrowException_whenInputContainsNegativeOffset() {

        int offset = -10;
        int pageSize = 10;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> manufacturerService.getPage(offset, pageSize));

        assertEquals("Offset must be a non-negative integer.", exception.getMessage());

        verify(manufacturerRepository, never()).findAll();

    }

    @Test
    void getPage_shouldThrowException_whenInputContainsNegativePageSize() {

        int offset = 10;
        int pageSize = -10;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> manufacturerService.getPage(offset, pageSize));

        assertEquals("Page size must be a positive integer.", exception.getMessage());

        verify(manufacturerRepository, never()).findAll();

    }
    @Test
    void getById_shouldReturnManufacturer_whenInputContainsExistingManufacturerId() {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer expectedManufacturer = new Manufacturer();

        expectedManufacturer.setManufacturerId(manufacturerId);
        expectedManufacturer.setManufacturerName(manufacturerName);

        when(manufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.ofNullable(expectedManufacturer));

        Manufacturer actualManufacturer = manufacturerService.getById(manufacturerId);

        assertEquals(expectedManufacturer, actualManufacturer);

        verify(manufacturerRepository).findById(manufacturerId);

    }

    @Test
    void getById_shouldThrowException_whenInputContainsNotExistingManufacturerId() {

        long manufacturerId = 100;

        when(manufacturerRepository.findById(manufacturerId))
                .thenReturn(Optional.empty());

        ManufacturerNotFoundException exception = assertThrows(ManufacturerNotFoundException.class, () -> manufacturerService.getById(manufacturerId));

        assertEquals("Manufacturer with Id " + manufacturerId + " not found.", exception.getMessage());

        verify(manufacturerRepository).findById(manufacturerId);

    }

}
