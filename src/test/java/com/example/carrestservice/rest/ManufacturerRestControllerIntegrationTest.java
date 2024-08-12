package com.example.carrestservice.rest;

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
@Sql(scripts = {"/sql/drop_data.sql", "/sql/insert_manufacturers.sql"})
public class ManufacturerRestControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    static Manufacturer manufacturer;

    @BeforeAll
    static void init() {
        manufacturer = new Manufacturer();
        manufacturer.setManufacturerId(1L);
        manufacturer.setManufacturerName("Test");
    }

    @Test
    public void createManufacturer_shouldReturnCreatedRequest() throws Exception {
        mockMvc.perform(post("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.manufacturerId").value(1))
                .andExpect(jsonPath("$.manufacturerName").value("Test"));
    }

    @Test
    public void updateManufacturer_shouldReturnNoContentRequest() throws Exception {
        mockMvc.perform(put("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removeManufacturerById_shouldReturnNoContentRequest() throws Exception {
        mockMvc.perform(delete("/manufacturers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getManufacturers_shouldReturnOKRequest() throws Exception {
        mockMvc.perform(get("/manufacturers")
                        .param("sortField", "manufacturerId")
                        .param("sortDirection", "ASC")
                        .param("offset", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].manufacturerId").value(1));
    }

    @Test
    public void getManufacturerById_shouldReturnOKRequest() throws Exception {
        mockMvc.perform(get("/manufacturers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manufacturerId").value(1))
                .andExpect(jsonPath("$.manufacturerName").value("Toyota"));
    }
}
