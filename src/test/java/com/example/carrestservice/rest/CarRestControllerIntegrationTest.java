package com.example.carrestservice.rest;

import com.example.carrestservice.entity.Car;
import com.example.carrestservice.entity.CarModel;
import com.example.carrestservice.entity.Category;
import com.example.carrestservice.entity.Manufacturer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_car_models.sql", "/sql/insert_categories.sql", "/sql/insert_manufacturers.sql", "/sql/insert_cars.sql"})
public class CarRestControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    static Car car;

    @BeforeAll
    static void init() {
        CarModel carModel = new CarModel();
        carModel.setCarModelId(10L);
        carModel.setCarModelName("Test");

        Category category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("Test");

        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(1L);
        manufacturer.setManufacturerName("Test");

        car = new Car.CarBuilder()
                .carId(1L)
                .manufacturer(manufacturer)
                .manufactureYear(2024)
                .carModel(carModel)
                .category(category)
                .build();
    }

    @Test
    public void createCar_shouldReturnCreatedRequest() throws Exception {
        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carId").value(1));
    }

    @Test
    public void updateCar_shouldReturnConflictRequest() throws Exception {
        mockMvc.perform(put("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isConflict());
    }

    @Test
    public void removeCarById_shouldReturnNoContentRequest() throws Exception {
        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getCars_shouldReturnOKRequest() throws Exception {
        mockMvc.perform(get("/cars")
                        .param("sortField", "carId")
                        .param("sortDirection", "ASC")
                        .param("offset", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].carId").value(1));
    }

    @Test
    public void getCarById_shouldReturnOKRequest() throws Exception {
        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(1));
    }
}
