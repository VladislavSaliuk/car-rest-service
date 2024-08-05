package com.example.carrestservice.rest;


import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.exception.CarModelNameException;
import com.example.carrestservice.exception.CarModelNotFoundException;
import com.example.carrestservice.exception.CategoryNotFoundException;
import com.example.carrestservice.service.CarModelService;
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
@WebMvcTest(CarModelRestController.class)
public class CarModelRestControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CarModelService carModelService;


    @Test
    void createCarModel_shouldReturnCreatedRequest_whenInputContainsCorrectData() throws Exception {

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelService.createCarModel(carModel))
                .thenReturn(carModel);

        mockMvc.perform(post("/car-models/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carModelId").value(carModel.getCarModelId()));

        verify(carModelService).createCarModel(carModel);
    }

    @Test
    void createCarModel_shouldReturnBadRequest_whenInputContainsNull() throws Exception {

        CarModel carModel = new CarModel();

        when(carModelService.createCarModel(any(CarModel.class)))
                .thenThrow(new IllegalArgumentException("Car model can not be null!"));

        mockMvc.perform(post("/car-models/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Car model can not be null!"));

        verify(carModelService).createCarModel(any(CarModel.class));
    }

    @Test
    void createCarModel_shouldReturnBadRequest_whenInputContainsAlreadyExistingCarModelName() throws Exception {

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelService.createCarModel(carModel))
                .thenThrow(new CarModelNameException("Car model name " + carModel.getCarModelName() + " already exists!"));

        mockMvc.perform(post("/car-models/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Car model name " + carModel.getCarModelName() + " already exists!"));

        verify(carModelService).createCarModel(carModel);

    }

    @Test
    void updateCarModel_shouldReturnCreatedRequest_whenInputContainsCorrectData() throws Exception {

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelService.updateCarModel(carModel))
                .thenReturn(carModel);

        mockMvc.perform(put("/car-models/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carModelId").value(carModel.getCarModelId()));

        verify(carModelService).updateCarModel(carModel);

    }

    @Test
    void updateCarModel_shouldReturnBadRequest_whenInputContainsNull() throws Exception {

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelService.updateCarModel(carModel))
                .thenThrow(new CarModelNameException("Car model name " + carModel.getCarModelName() + " already exists!"));

        mockMvc.perform(put("/car-models/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Car model name " + carModel.getCarModelName() + " already exists!"));

        verify(carModelService).updateCarModel(carModel);

    }

    @Test
    void updateCarModel_shouldReturnBadRequest_whenInputContainsCarModelWithNotExistingCarModelId() throws Exception {

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelService.updateCarModel(carModel))
                .thenThrow(new CarModelNameException("Car model name " + carModel.getCarModelName() + " already exists!"));

        mockMvc.perform(put("/car-models/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Car model name " + carModel.getCarModelName() + " already exists!"));

        verify(carModelService).updateCarModel(carModel);

    }

    @Test
    void updateCarModel_shouldReturnBadRequest_whenInputAlreadyExistingCarModelName() throws Exception {

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelService.updateCarModel(carModel))
                .thenThrow(new CarModelNotFoundException("Car model with Id " + carModel.getCarModelId() + " not found."));

        mockMvc.perform(put("/car-models/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Car model with Id " + carModel.getCarModelId() + " not found."));

        verify(carModelService).updateCarModel(carModel);

    }

    @Test
    void removeCarModelById_shouldReturnOkRequest_whenInputContainsExistingCarModelId() throws Exception {

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelService.removeById(carModelId))
                .thenReturn(carModel);

        mockMvc.perform(delete("/car-models/remove/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carModelId").value(carModel.getCarModelId()));

        verify(carModelService).removeById(carModelId);

    }

    @Test
    void removeCarModelById_shouldReturnBadRequest_whenInputContainsNotExistingCarModelId() throws Exception {

        long carModelId = 100;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelService.removeById(carModelId))
                .thenThrow(new CarModelNotFoundException("Car model with Id " + carModelId + " not found."));

        mockMvc.perform(delete("/car-models/remove/100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Car model with Id " + carModel.getCarModelId() + " not found."));

        verify(carModelService).removeById(carModelId);

    }

    @Test
    void getCarModels_shouldReturnOkRequest() throws Exception {

        when(carModelService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/car-models"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(carModelService).getAll();

    }

    @Test
    void getSortedCarModels_shouldReturnOkRequest_whenInputContainsCorrectData() throws Exception {

        String sortField = "carModelName";
        String sortDirection = "ASC";

        when(carModelService.getAll(sortDirection, sortField))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/car-models/sort")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDirection))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(carModelService).getAll(sortDirection, sortField);
    }
    @Test
    void getSortedCarModels_shouldReturnOkRequest_whenInputContainsInCorrectSortField() throws Exception {

        String sortField = "test car model field";
        String sortDirection = "ASC";

        when(carModelService.getAll(sortDirection, sortField))
                .thenThrow(new IllegalArgumentException("Invalid sort field : " + sortField));

        mockMvc.perform(get("/car-models/sort")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDirection))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Invalid sort field : " + sortField));

        verify(carModelService).getAll(sortDirection, sortField);
    }


    @Test
    void getCarModelPage_shouldReturnOkRequest_whenInputContainsCorrectData() throws Exception {

        int offset = 10;
        int pageSize = 10;

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        Page<CarModel> carModelPage = new PageImpl<>(List.of(carModel));

        when(carModelService.getPage(offset, pageSize))
                .thenReturn(carModelPage);

        mockMvc.perform(get("/car-models/pagination")
                        .param("offset", String.valueOf(offset))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].carModelId").value(carModel.getCarModelId()));

        verify(carModelService).getPage(offset, pageSize);

    }

    @Test
    void getCarModelPage_shouldReturnBadRequestRequest_whenInputContainsNegativeOffset() throws Exception {

        int offset = -10;
        int pageSize = 10;

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelService.getPage(offset, pageSize))
                .thenThrow(new IllegalArgumentException("Offset must be a non-negative integer."));

        mockMvc.perform(get("/car-models/pagination")
                        .param("offset", String.valueOf(offset))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Offset must be a non-negative integer."));

        verify(carModelService).getPage(offset, pageSize);

    }

    @Test
    void getCarModelPage_shouldReturnBadRequestRequest_whenInputContainsNegativePageSize() throws Exception {

        int offset = 10;
        int pageSize = -10;

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelService.getPage(offset, pageSize))
                .thenThrow(new IllegalArgumentException("Page size must be a positive integer."));

        mockMvc.perform(get("/car-models/pagination")
                        .param("offset", String.valueOf(offset))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Page size must be a positive integer."));

        verify(carModelService).getPage(offset, pageSize);

    }

    @Test
    void getCarModelById_shouldReturnOkRequest_whenInputContainsExistingCategoryId() throws Exception {

        long carModelId = 1;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelService.getById(carModelId))
                .thenReturn(carModel);

        mockMvc.perform(get("/car-models/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carModelId").value(carModel.getCarModelId()));

        verify(carModelService).getById(carModelId);

    }

    @Test
    void getCarModelById_shouldReturnBadRequest_whenInputContainsNotExistingCategoryId() throws Exception {

        long carModelId = 100;
        String carModelName = "Test car model name";

        CarModel carModel = new CarModel();
        carModel.setCarModelId(carModelId);
        carModel.setCarModelName(carModelName);

        when(carModelService.getById(carModelId))
                .thenThrow(new CategoryNotFoundException("Car model with Id " + carModelId + " not found."));

        mockMvc.perform(get("/car-models/100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Car model with Id " + carModelId + " not found."));

        verify(carModelService).getById(carModelId);

    }


}
