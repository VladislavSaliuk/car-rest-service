package com.example.carrestservice.rest;

import com.example.carrestservice.entity.CarModel;
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
@Sql(scripts = {"/sql/drop_data.sql","/sql/insert_car_models.sql"})
public class CarModelRestControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    static CarModel carModel;

    @BeforeAll
    static void init() {
        carModel = new CarModel();
        carModel.setCarModelId(1L);
        carModel.setCarModelName("Test");
    }

    @Test
    public void createCarModel_shouldReturnCreatedRequest() throws Exception {
        mockMvc.perform(post("/car-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carModelId").value(1))
                .andExpect(jsonPath("$.carModelName").value("Test"));
    }

    @Test
    public void updateCarModel_shouldReturnNoContentRequest() throws Exception {
        mockMvc.perform(put("/car-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removeCarModelById_shouldReturnNoContentRequest() throws Exception {
        mockMvc.perform(delete("/car-models/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getCarModels_shouldReturnOKRequest() throws Exception {
        mockMvc.perform(get("/car-models")
                        .param("sortField", "carModelId")
                        .param("sortDirection", "ASC")
                        .param("offset", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].carModelId").value(1));
    }

    @Test
    public void getCarModelById_shouldReturnOKRequest() throws Exception {
        mockMvc.perform(get("/car-models/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carModelId").value(1))
                .andExpect(jsonPath("$.carModelName").value("Camry"));
    }
}
