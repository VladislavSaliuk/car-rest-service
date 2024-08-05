package com.example.carrestservice.rest;

import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.ManufacturerNameException;
import com.example.carrestservice.exception.ManufacturerNotFoundException;
import com.example.carrestservice.service.ManufacturerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ManufacturerRestController.class)
public class ManufacturerRestControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ManufacturerService manufacturerService;

    @Test
    void createManufacturer_shouldReturnCreatedRequest_whenInputContainsCorrectData() throws Exception {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        when(manufacturerService.createManufacturer(manufacturer))
                .thenReturn(manufacturer);

        mockMvc.perform(post("/manufacturers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.manufacturerId").value(manufacturer.getManufacturerId()));

        verify(manufacturerService).createManufacturer(manufacturer);
    }

    @Test
    void createManufacturer_shouldReturnBadRequest_whenInputContainsNull() throws Exception {

        Manufacturer manufacturer = new Manufacturer();

        when(manufacturerService.createManufacturer(any(Manufacturer.class)))
                .thenThrow(new IllegalArgumentException("Manufacturer cannot be null!"));

        mockMvc.perform(post("/manufacturers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Manufacturer cannot be null!"));

        verify(manufacturerService).createManufacturer(any(Manufacturer.class));
    }

    @Test
    void createManufacturer_shouldReturnBadRequest_whenInputContainsAlreadyExistingManufacturerName() throws Exception {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        when(manufacturerService.createManufacturer(manufacturer))
                .thenThrow(new ManufacturerNameException("Manufacturer name " + manufacturer.getManufacturerName() + " already exists!"));

        mockMvc.perform(post("/manufacturers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Manufacturer name " + manufacturer.getManufacturerName() + " already exists!"));

        verify(manufacturerService).createManufacturer(manufacturer);
    }

    @Test
    void updateManufacturer_shouldReturnCreatedRequest_whenInputContainsCorrectData() throws Exception {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        when(manufacturerService.updateManufacturer(manufacturer))
                .thenReturn(manufacturer);

        mockMvc.perform(put("/manufacturers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.manufacturerId").value(manufacturer.getManufacturerId()));

        verify(manufacturerService).updateManufacturer(manufacturer);
    }

    @Test
    void updateManufacturer_shouldReturnBadRequest_whenInputContainsNull() throws Exception {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        when(manufacturerService.updateManufacturer(manufacturer))
                .thenThrow(new ManufacturerNameException("Manufacturer name " + manufacturer.getManufacturerName() + " already exists!"));

        mockMvc.perform(put("/manufacturers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Manufacturer name " + manufacturer.getManufacturerName() + " already exists!"));

        verify(manufacturerService).updateManufacturer(manufacturer);
    }

    @Test
    void updateManufacturer_shouldReturnBadRequest_whenInputContainsManufacturerWithNotExistingManufacturerId() throws Exception {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        when(manufacturerService.updateManufacturer(manufacturer))
                .thenThrow(new ManufacturerNameException("Manufacturer name " + manufacturer.getManufacturerName() + " already exists!"));

        mockMvc.perform(put("/manufacturers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Manufacturer name " + manufacturer.getManufacturerName() + " already exists!"));

        verify(manufacturerService).updateManufacturer(manufacturer);
    }

    @Test
    void updateManufacturer_shouldReturnBadRequest_whenInputAlreadyExistingManufacturerName() throws Exception {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        when(manufacturerService.updateManufacturer(manufacturer))
                .thenThrow(new ManufacturerNotFoundException("Manufacturer with Id " + manufacturer.getManufacturerId() + " not found."));

        mockMvc.perform(put("/manufacturers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Manufacturer with Id " + manufacturer.getManufacturerId() + " not found."));

        verify(manufacturerService).updateManufacturer(manufacturer);
    }

    @Test
    void removeManufacturerById_shouldReturnOkRequest_whenInputContainsExistingManufacturerId() throws Exception {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        when(manufacturerService.removeById(manufacturerId))
                .thenReturn(manufacturer);

        mockMvc.perform(delete("/manufacturers/remove/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manufacturerId").value(manufacturer.getManufacturerId()));

        verify(manufacturerService).removeById(manufacturerId);
    }

    @Test
    void removeManufacturerById_shouldReturnBadRequest_whenInputContainsNotExistingManufacturerId() throws Exception {

        long manufacturerId = 100;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        when(manufacturerService.removeById(manufacturerId))
                .thenThrow(new ManufacturerNotFoundException("Manufacturer with Id " + manufacturerId + " not found."));

        mockMvc.perform(delete("/manufacturers/remove/100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Manufacturer with Id " + manufacturerId + " not found."));

        verify(manufacturerService).removeById(manufacturerId);
    }

    @Test
    void getManufacturers_shouldReturnOkRequest() throws Exception {

        when(manufacturerService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/manufacturers"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(manufacturerService).getAll();
    }

    @Test
    void getSortedManufacturers_shouldReturnOkRequest_whenInputContainsCorrectData() throws Exception {

        String sortField = "manufacturerName";
        String sortDirection = "ASC";

        when(manufacturerService.getAll(sortDirection, sortField))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/manufacturers/sort")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDirection))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(manufacturerService).getAll(sortDirection, sortField);
    }

    @Test
    void getSortedManufacturers_shouldReturnBadRequest_whenInputContainsIncorrectSortField() throws Exception {

        String sortField = "test manufacturer field";
        String sortDirection = "ASC";

        when(manufacturerService.getAll(sortDirection, sortField))
                .thenThrow(new IllegalArgumentException("Invalid sort field: " + sortField));

        mockMvc.perform(get("/manufacturers/sort")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDirection))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Invalid sort field: " + sortField));

        verify(manufacturerService).getAll(sortDirection, sortField);
    }

    @Test
    void getManufacturerPage_shouldReturnOkRequest_whenInputContainsCorrectData() throws Exception {

        int offset = 10;
        int pageSize = 10;

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        Page<Manufacturer> manufacturerPage = new PageImpl<>(List.of(manufacturer));

        when(manufacturerService.getPage(offset, pageSize))
                .thenReturn(manufacturerPage);

        mockMvc.perform(get("/manufacturers/pagination")
                        .param("offset", String.valueOf(offset))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].manufacturerId").value(manufacturer.getManufacturerId()));

        verify(manufacturerService).getPage(offset, pageSize);
    }

    @Test
    void getManufacturerPage_shouldReturnBadRequest_whenInputContainsNegativeOffset() throws Exception {

        int offset = -10;
        int pageSize = 10;

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        when(manufacturerService.getPage(offset, pageSize))
                .thenThrow(new IllegalArgumentException("Offset must be a non-negative integer."));

        mockMvc.perform(get("/manufacturers/pagination")
                        .param("offset", String.valueOf(offset))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Offset must be a non-negative integer."));

        verify(manufacturerService).getPage(offset, pageSize);
    }

    @Test
    void getManufacturerPage_shouldReturnBadRequest_whenInputContainsNegativePageSize() throws Exception {

        int offset = 10;
        int pageSize = -10;

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        when(manufacturerService.getPage(offset, pageSize))
                .thenThrow(new IllegalArgumentException("Page size must be a positive integer."));

        mockMvc.perform(get("/manufacturers/pagination")
                        .param("offset", String.valueOf(offset))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Page size must be a positive integer."));

        verify(manufacturerService).getPage(offset, pageSize);
    }

    @Test
    void getManufacturerById_shouldReturnOkRequest_whenInputContainsExistingManufacturerId() throws Exception {

        long manufacturerId = 1;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        when(manufacturerService.getById(manufacturerId))
                .thenReturn(manufacturer);

        mockMvc.perform(get("/manufacturers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manufacturerId").value(manufacturer.getManufacturerId()));

        verify(manufacturerService).getById(manufacturerId);
    }

    @Test
    void getManufacturerById_shouldReturnBadRequest_whenInputContainsNotExistingManufacturerId() throws Exception {

        long manufacturerId = 100;
        String manufacturerName = "Test manufacturer name";

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(manufacturerId);
        manufacturer.setManufacturerName(manufacturerName);

        when(manufacturerService.getById(manufacturerId))
                .thenThrow(new ManufacturerNotFoundException("Manufacturer with Id " + manufacturerId + " not found."));

        mockMvc.perform(get("/manufacturers/100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Manufacturer with Id " + manufacturerId + " not found."));

        verify(manufacturerService).getById(manufacturerId);
    }
}
