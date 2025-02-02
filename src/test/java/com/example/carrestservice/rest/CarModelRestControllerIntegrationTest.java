package com.example.carrestservice.rest;

import com.example.carrestservice.entity.CarModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_car_models.sql"})
public class CarModelRestControllerIntegrationTest {


    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    CarModel carModel;

    @BeforeEach
    public void setUp() {
        carModel = new CarModel();
        carModel.setCarModelId(1L);
        carModel.setCarModelName("Test");
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void createCarModel_shouldReturnCreatedStatus() throws Exception {
        mockMvc.perform(post("/car-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carModelId").value(1))
                .andExpect(jsonPath("$.carModelName").value("Test"));
    }

    @Test
    public void updateCarModel_shouldReturnNoContentStatus() throws Exception {

        mockMvc.perform(put("/car-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removeCarModelById_shouldReturnNoContentStatus() throws Exception {
        String response = mockMvc.perform(post("/car-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long carModelId = objectMapper.readTree(response).get("carModelId").asLong();

        mockMvc.perform(delete("/car-models/" + carModelId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getCarModels_shouldReturnOKStatus() throws Exception {
        mockMvc.perform(post("/car-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/car-models")
                        .param("sortField", "carModelId")
                        .param("sortDirection", "ASC")
                        .param("offset", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].carModelId").value(1));
    }

    @Test
    public void getCarModelById_shouldReturnOKStatus() throws Exception {
        String response = mockMvc.perform(post("/car-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carModel)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long carModelId = objectMapper.readTree(response).get("carModelId").asLong();

        mockMvc.perform(get("/car-models/" + carModelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carModelId").value(carModelId))
                .andExpect(jsonPath("$.carModelName").value("Test"));
    }
}
