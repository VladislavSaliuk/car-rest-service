package com.example.carrestservice.rest;

import com.example.carrestservice.entity.Car;
import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Category;
import com.example.carrestservice.entity.Manufacturer;
import com.example.carrestservice.exception.ApiError;
import com.example.carrestservice.exception.CarNotFoundException;
import com.example.carrestservice.service.CarService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CarRestController.class)
public class CarRestControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CarService carService;

    @Test
    void createCar_shouldReturnOkRequest_whenInputContainsCorrectData() throws Exception {

        long carId = 1;
        Manufacturer manufacturer = new Manufacturer();
        CarModel carModel = new CarModel();
        Category category = new Category();

        Car car = new Car(manufacturer, 2022, carModel, category);
        car.setCarId(carId);

        when(carService.createCar(car)).thenReturn(car);

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carId").value(car.getCarId()));

        verify(carService).createCar(car);
    }

    @Test
    void createCar_shouldReturnBadRequest_whenInputContainsNull() throws Exception {

        Car car = new Car(null, 0, null, null);

        when(carService.createCar(any(Car.class)))
                .thenThrow(new IllegalArgumentException("Car cannot be null!"));

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Car cannot be null!"));

        verify(carService).createCar(any(Car.class));
    }

    @Test
    void updateCar_shouldReturnOkRequest_whenInputContainsCorrectData() throws Exception {

        long carId = 1;
        Manufacturer manufacturer = new Manufacturer();
        CarModel carModel = new CarModel();
        Category category = new Category();

        Car car = new Car(manufacturer, 2022, carModel, category);
        car.setCarId(carId);

        when(carService.updateCar(car)).thenReturn(car);

        mockMvc.perform(put("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(car.getCarId()));

        verify(carService).updateCar(car);
    }

    @Test
    void updateCar_shouldReturnBadRequest_whenInputContainsNull() throws Exception {

        long carId = 1;
        Manufacturer manufacturer = new Manufacturer();
        CarModel carModel = new CarModel();
        Category category = new Category();

        Car car = new Car(manufacturer, 2022, carModel, category);
        car.setCarId(carId);

        when(carService.updateCar(car))
                .thenThrow(new IllegalArgumentException("Car cannot be null!"));

        mockMvc.perform(put("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Car cannot be null!"));

        verify(carService).updateCar(car);
    }

    @Test
    void removeCarById_shouldReturnOkRequest_whenInputContainsExistingCarId() throws Exception {

        long carId = 1;
        Manufacturer manufacturer = new Manufacturer();
        CarModel carModel = new CarModel();
        Category category = new Category();

        Car car = new Car(manufacturer, 2022, carModel, category);
        car.setCarId(carId);

        when(carService.removeById(carId)).thenReturn(car);

        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(car.getCarId()));

        verify(carService).removeById(carId);
    }

    @Test
    void removeCarById_shouldReturnBadRequest_whenInputContainsNotExistingCarId() throws Exception {

        long carId = 100;

        when(carService.removeById(carId))
                .thenThrow(new CarNotFoundException("Car with Id " + carId + " not found."));

        mockMvc.perform(delete("/cars/100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Car with Id " + carId + " not found."));

        verify(carService).removeById(carId);
    }

    @Test
    void getCars_shouldReturnOkRequest() throws Exception {

        Manufacturer manufacturer = new Manufacturer();
        CarModel carModel = new CarModel();
        Category category = new Category();
        Car car = new Car(manufacturer, 2022, carModel, category);

        when(carService.getAll()).thenReturn(Collections.singletonList(car));

        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].carId").value(car.getCarId()));

        verify(carService).getAll();
    }

    @Test
    void getSortedCars_shouldReturnOkRequest_whenInputContainsCorrectData() throws Exception {

        String sortField = "carId";
        String sortDirection = "DESC";

        Manufacturer manufacturer = new Manufacturer();
        CarModel carModel = new CarModel();
        Category category = new Category();
        Car car = new Car(manufacturer, 2022, carModel, category);

        when(carService.getAll(sortDirection, sortField))
                .thenReturn(Collections.singletonList(car));

        mockMvc.perform(get("/cars/sort")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDirection))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].carId").value(car.getCarId()));

        verify(carService).getAll(sortDirection, sortField);
    }

    @Test
    void getSortedCars_shouldReturnBadRequest_whenInputContainsIncorrectSortField() throws Exception {

        String sortField = "unknownField";
        String sortDirection = "DESC";

        when(carService.getAll(sortDirection, sortField))
                .thenThrow(new IllegalArgumentException("Invalid sort field: " + sortField));

        mockMvc.perform(get("/cars/sort")
                        .param("sortField", sortField)
                        .param("sortDirection", sortDirection))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Invalid sort field: " + sortField));

        verify(carService).getAll(sortDirection, sortField);
    }

    @Test
    void getCarPage_shouldReturnOkRequest_whenInputContainsCorrectData() throws Exception {

        int offset = 10;
        int pageSize = 10;

        Manufacturer manufacturer = new Manufacturer();
        CarModel carModel = new CarModel();
        Category category = new Category();
        Car car = new Car(manufacturer, 2022, carModel, category);

        Page<Car> carPage = new PageImpl<>(List.of(car));

        when(carService.getPage(offset, pageSize)).thenReturn(carPage);

        mockMvc.perform(get("/cars/pagination")
                        .param("offset", String.valueOf(offset))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].carId").value(car.getCarId()));

        verify(carService).getPage(offset, pageSize);
    }

    @Test
    void getCarPage_shouldReturnBadRequest_whenInputContainsNegativeOffset() throws Exception {

        int offset = -10;
        int pageSize = 10;

        when(carService.getPage(offset, pageSize))
                .thenThrow(new IllegalArgumentException("Offset must be a non-negative integer."));

        mockMvc.perform(get("/cars/pagination")
                        .param("offset", String.valueOf(offset))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Offset must be a non-negative integer."));

        verify(carService).getPage(offset, pageSize);
    }

    @Test
    void getCarPage_shouldReturnBadRequest_whenInputContainsNegativePageSize() throws Exception {

        int offset = 10;
        int pageSize = -10;

        when(carService.getPage(offset, pageSize))
                .thenThrow(new IllegalArgumentException("Page size must be a positive integer."));

        mockMvc.perform(get("/cars/pagination")
                        .param("offset", String.valueOf(offset))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Page size must be a positive integer."));

        verify(carService).getPage(offset, pageSize);
    }

    @Test
    void getCarById_shouldReturnOkRequest_whenInputContainsExistingCarId() throws Exception {

        long carId = 1;
        Manufacturer manufacturer = new Manufacturer();
        CarModel carModel = new CarModel();
        Category category = new Category();
        Car car = new Car(manufacturer, 2022, carModel, category);
        car.setCarId(carId);

        when(carService.getById(carId)).thenReturn(car);

        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(car.getCarId()));

        verify(carService).getById(carId);
    }

    @Test
    void getCarById_shouldReturnBadRequest_whenInputContainsNotExistingCarId() throws Exception {

        long carId = 100;

        when(carService.getById(carId))
                .thenThrow(new CarNotFoundException("Car with Id " + carId + " not found."));

        mockMvc.perform(get("/cars/100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Car with Id " + carId + " not found."));

        verify(carService).getById(carId);
    }
}
