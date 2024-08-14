package com.example.carrestservice.rest;

import com.example.carrestservice.entity.Manufacturer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_manufacturers.sql"})
public class ManufacturerRestControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    Manufacturer manufacturer;

    @BeforeEach
    public void setUp() {
        manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(1L);
        manufacturer.setManufacturerName("Test");
    }

    @Test
    public void createManufacturer_shouldReturnCreatedStatus() throws Exception {
        mockMvc.perform(post("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.manufacturerId").value(1))
                .andExpect(jsonPath("$.manufacturerName").value("Test"));
    }

    @Test
    public void updateManufacturer_shouldReturnNoContentStatus() throws Exception {

        mockMvc.perform(put("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removeManufacturerById_shouldReturnNoContentStatus() throws Exception {
        String response = mockMvc.perform(post("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long manufacturerId = objectMapper.readTree(response).get("manufacturerId").asLong();

        mockMvc.perform(delete("/manufacturers/" + manufacturerId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getManufacturers_shouldReturnOKStatus() throws Exception {
        mockMvc.perform(post("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/manufacturers")
                        .param("sortField", "manufacturerId")
                        .param("sortDirection", "ASC")
                        .param("offset", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].manufacturerId").value(1));
    }

    @Test
    public void getManufacturerById_shouldReturnOKStatus() throws Exception {
        String response = mockMvc.perform(post("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long manufacturerId = objectMapper.readTree(response).get("manufacturerId").asLong();

        mockMvc.perform(get("/manufacturers/" + manufacturerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manufacturerId").value(manufacturerId))
                .andExpect(jsonPath("$.manufacturerName").value("Test"));
    }
}
